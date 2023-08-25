package com.easymedia.error.hotel.utility.sim;

import java.io.UnsupportedEncodingException;

public class SeedCipher
{
	private static final int SEED_BLOCK_SIZE = 16;	
	
	private static String authkey = "gksrnrghksrudrhdeks";
	
	/**
	 * SEED algorithm to encrypt the data.
	 * @param data Target Data
	 * @param charset Data character set
	 * @return Encrypted data
	 * @throws UnsupportedEncodingException If character is not supported
	 */
	public static String encrypt(String data, String charset) throws UnsupportedEncodingException 
	{
		data = data.trim();
		byte[] key = authkey.getBytes();
		byte[] encrypt = null;
		
		if (charset == null) 
		{
			encrypt = BlockPadding.getInstance().addPadding(data.getBytes(), SEED_BLOCK_SIZE);
		} 
		else 
		{
			encrypt = BlockPadding.getInstance().addPadding(data.getBytes(charset), SEED_BLOCK_SIZE);
		}
		
		int pdwRoundKey[] = new int[32];
		
		SEED_KISA.SeedRoundKey(pdwRoundKey, key);
		
		int blockCount = encrypt.length / SEED_BLOCK_SIZE;
		
		for (int i = 0; i < blockCount; i++)
		{
			byte sBuffer[] = new byte[SEED_BLOCK_SIZE];
			byte tBuffer[] = new byte[SEED_BLOCK_SIZE];
			
			System.arraycopy(encrypt, (i * SEED_BLOCK_SIZE), sBuffer, 0, SEED_BLOCK_SIZE);
			
			SEED_KISA.SeedEncrypt(sBuffer, pdwRoundKey, tBuffer);
			
			System.arraycopy(tBuffer, 0, encrypt, (i * SEED_BLOCK_SIZE), tBuffer.length);
		}
		
		return Base64.toString(encrypt);
	}
	
	/**
	 * ARIA algorithm to decrypt the data.
	 * @param data Target Data
	 * @param charset Data character set
	 * @return Decrypted data
	 * @throws UnsupportedEncodingException If character is not supported
	 */
	public static String decrypt(String data,  String charset) throws UnsupportedEncodingException 
	{
		if (data == null)
		{
			return "";
		}
		
		data = data.trim();
		
		byte[] key = authkey.getBytes();		// 키 값
		
		int pdwRoundKey[] = new int[32];
		SEED_KISA.SeedRoundKey(pdwRoundKey, key);
		
		byte[] decrypt = Base64.toByte(data);
		int blockCount = decrypt.length / SEED_BLOCK_SIZE;
		
		for (int i = 0; i < blockCount; i++ ) 
		{
			byte sBuffer[] = new byte[SEED_BLOCK_SIZE];
			byte tBuffer[] = new byte[SEED_BLOCK_SIZE];
			
			System.arraycopy(decrypt, (i * SEED_BLOCK_SIZE), sBuffer, 0, SEED_BLOCK_SIZE);
			
			SEED_KISA.SeedDecrypt(sBuffer, pdwRoundKey, tBuffer);
			
			System.arraycopy(tBuffer, 0, decrypt, (i * SEED_BLOCK_SIZE), tBuffer.length);
		}
		
		String rtn = "";
		
		if( charset == null ) 
		{
			rtn =  new String(BlockPadding.getInstance().removePadding(decrypt, SEED_BLOCK_SIZE));
		}
		else 
		{
			rtn =  new String(BlockPadding.getInstance().removePadding(decrypt, SEED_BLOCK_SIZE), charset);
		}
		
		rtn = rtn.trim();
		
		return rtn;
	}
	
	/*
	 * 단방향 암호화
	 */
	public static String oneencrypt(String data) throws Exception
	{
		return EgovFileScrty.encryptPassword(data);
	}
}