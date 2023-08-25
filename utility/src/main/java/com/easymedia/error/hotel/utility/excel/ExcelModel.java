package com.easymedia.error.hotel.utility.excel;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.ToString;

@Getter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ExcelModel implements Serializable {

    @Builder.Default
    private String fileName = "excel";

    @Singular
    private List<Sheet> sheets;

    @Getter
    @ToString
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class Sheet implements Serializable {

        private String sheetName;
        private String title;
        @Builder.Default
        private Style titleStyle = Style.titleBuilder().build();
        @Singular
        private List<Column> columns;
        private List<?> data;

        public boolean isTitle() {
            return StringUtils.isNoneBlank(this.title);
        }

        public boolean hasMultiHeader() {
            return this.columns.stream()
                    .anyMatch(Column::hasMultiHeader);
        }

        @Getter
        @ToString
        @Builder
        @NoArgsConstructor(access = AccessLevel.PROTECTED)
        @AllArgsConstructor
        public static class Column implements Serializable {

            private String headerName;
            private String key;
            private int width;
            @Builder.Default
            private boolean hidden = false;
            private String memo;
            private MultiHeader multiHeader;
            @Builder.Default
            private Style headerStyle = Style.headerBuilder().build();
            @Builder.Default
            private Style bodyStyle = Style.builder().build();

            public boolean hasMemo() {
                return StringUtils.isNotBlank(this.memo);
            }

            public boolean hasMultiHeader() {
                return this.multiHeader != null;
            }
        }

        @Getter
        @ToString
        @Builder
        @NoArgsConstructor(access = AccessLevel.PROTECTED)
        @AllArgsConstructor
        public static class MultiHeader implements Serializable {

            private String headerName;
            @Builder.Default
            private int mergeColumnCount = 1;
            @Builder.Default
            private Style headerStyle = Style.headerBuilder().build();

            public int getMergeColumnCount() {
                return this.mergeColumnCount < 1 ? 0 : this.mergeColumnCount - 1;
            }
        }

        @Getter
        @ToString
        @NoArgsConstructor(access = AccessLevel.PROTECTED)
        public static class Style implements Serializable {
            private boolean wrapText;
            private HorizontalAlignment horizontalAlignment;
            private VerticalAlignment verticalAlignment;
            private boolean fontBold;
            private short fontHeight;
            private short fontColor;
            private short fillForegroundColor;
            private FillPatternType fillPattern;
            private BorderStyle borderTop;
            private BorderStyle borderRight;
            private BorderStyle borderLeft;
            private BorderStyle borderBottom;

            @Builder
            public Style(Boolean wrapText, HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment, Boolean fontBold, Short fontHeight, Short fontColor, Short fillForegroundColor, FillPatternType fillPattern, BorderStyle borderTop, BorderStyle borderRight, BorderStyle borderLeft, BorderStyle borderBottom) {
                this.wrapText = Optional.ofNullable(wrapText).orElse(true);
                this.horizontalAlignment = Optional.ofNullable(horizontalAlignment).orElse(HorizontalAlignment.CENTER);
                this.verticalAlignment = Optional.ofNullable(verticalAlignment).orElse(VerticalAlignment.CENTER);
                this.fontBold = Optional.ofNullable(fontBold).orElse(false);
                this.fontHeight = Optional.ofNullable(fontHeight).orElse((short) 220);
                this.fontColor = Optional.ofNullable(fontColor).orElse(IndexedColors.AUTOMATIC.getIndex());
                this.fillForegroundColor = Optional.ofNullable(fillForegroundColor).orElse(IndexedColors.AUTOMATIC.getIndex());
                this.fillPattern = Optional.ofNullable(fillPattern).orElse(FillPatternType.NO_FILL);
                this.borderTop = Optional.ofNullable(borderTop).orElse(BorderStyle.NONE);
                this.borderRight = Optional.ofNullable(borderRight).orElse(BorderStyle.NONE);
                this.borderLeft = Optional.ofNullable(borderLeft).orElse(BorderStyle.NONE);
                this.borderBottom = Optional.ofNullable(borderBottom).orElse(BorderStyle.NONE);
            }

            @Builder(builderMethodName = "titleBuilder", builderClassName = "titleBuilder")
            public static Style title(Boolean wrapText, HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment, Boolean fontBold, Short fontHeight, Short fontColor, Short fillForegroundColor, FillPatternType fillPattern, BorderStyle borderTop, BorderStyle borderRight, BorderStyle borderLeft, BorderStyle borderBottom) {
                return Style.builder()
                        .wrapText(wrapText)
                        .horizontalAlignment(horizontalAlignment)
                        .verticalAlignment(verticalAlignment)
                        .fontBold(Optional.ofNullable(fontBold).orElse(true))
                        .fontHeight(Optional.ofNullable(fontHeight).orElse((short) 400))
                        .fontColor(fontColor)
                        .fillForegroundColor(fillForegroundColor)
                        .fillPattern(fillPattern)
                        .borderTop(borderTop)
                        .borderRight(borderRight)
                        .borderLeft(borderLeft)
                        .borderBottom(borderBottom)
                        .build();
            }

            @Builder(builderMethodName = "headerBuilder", builderClassName = "headerBuilder")
            public static Style header(Boolean wrapText, HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment, Boolean fontBold, Short fontHeight, Short fontColor, Short fillForegroundColor, FillPatternType fillPattern, BorderStyle borderTop, BorderStyle borderRight, BorderStyle borderLeft, BorderStyle borderBottom) {
                return Style.builder()
                        .wrapText(wrapText)
                        .horizontalAlignment(horizontalAlignment)
                        .verticalAlignment(verticalAlignment)
                        .fontBold(Optional.ofNullable(fontBold).orElse(true))
                        .fontHeight(fontHeight)
                        .fontColor(fontColor)
                        .fillForegroundColor(Optional.ofNullable(fillForegroundColor).orElse(IndexedColors.GREY_25_PERCENT.getIndex()))
                        .fillPattern(Optional.ofNullable(fillPattern).orElse(FillPatternType.SOLID_FOREGROUND))
                        .borderTop(Optional.ofNullable(borderTop).orElse(BorderStyle.THIN))
                        .borderRight(Optional.ofNullable(borderRight).orElse(BorderStyle.THIN))
                        .borderLeft(Optional.ofNullable(borderLeft).orElse(BorderStyle.THIN))
                        .borderBottom(Optional.ofNullable(borderBottom).orElse(BorderStyle.THIN))
                        .build();
            }

            public CellStyle createCellStyle(Workbook workbook) {
                CellStyle style = workbook.createCellStyle();
                Font font = workbook.createFont();

                style.setWrapText(this.wrapText);
                // 정렬
                style.setAlignment(this.horizontalAlignment);
                style.setVerticalAlignment(this.verticalAlignment);
                // 폰트
                font.setBold(this.fontBold);
                font.setFontHeight(this.fontHeight);
                font.setColor(this.fontColor);
                // 배경
                style.setFillForegroundColor(this.fillForegroundColor);
                style.setFillPattern(this.fillPattern);
                // 테두리
                style.setBorderTop(this.borderTop);
                style.setBorderRight(this.borderRight);
                style.setBorderLeft(this.borderLeft);
                style.setBorderBottom(this.borderBottom);

                style.setFont(font);
                return style;
            }
        }
    }
}
