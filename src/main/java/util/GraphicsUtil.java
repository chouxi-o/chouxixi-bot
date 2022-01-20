package util;

import sun.font.FontDesignMetrics;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GraphicsUtil {

    /**
     * 根据文本框宽度 作图
     * param font 字体
     * param content 内容
     * param contentWidth 文本框宽度
     * param contentHeight 文本框高度&#xff08;未使用&#xff09;
     * param graphics
     * param x 绘制位置x
     * param y 绘制位置y
     * param color 文字颜色
     * throws IOException
     */
    public static void writeFont(Font font, String content, Integer contentWidth, Integer contentHeight, Graphics2D graphics, int x, int y, Color color) {
        int rowWidth = 0;   //已用字当前行宽度
        int tempWidth;
        String lineString = "";
        List<String> contentLineList = new ArrayList<>();
        List<Integer> contentLineWidth = new ArrayList<>();
        FontDesignMetrics metrics = FontDesignMetrics.getMetrics(font);
        int fontHeight = metrics.getHeight();
        //每个新增字体宽度增加后如果超过文本框宽度就换行&#xff0c;没超过就不换行
        for (int i = 0; i < content.length(); i++) {
            tempWidth = rowWidth;//存之前的情况
            rowWidth = rowWidth + metrics.charWidth(content.charAt(i));

            if (rowWidth > contentWidth) {
                contentLineList.add(lineString);
                contentLineWidth.add(tempWidth);
                rowWidth = metrics.charWidth(content.charAt(i));
                lineString = "" + content.charAt(i);
            } else {
                lineString = lineString + content.charAt(i);
            }

            //最后一个字
            if (i == content.length() - 1) {
                contentLineList.add(lineString);
                contentLineWidth.add(rowWidth);
            }
        }
        graphics.setFont(font);
        graphics.setColor(color);

        for (int i = 0; i < contentLineList.size(); i++) {
            graphics.drawString(contentLineList.get(i), (contentWidth - contentLineWidth.get(i)) / 2 + x, fontHeight + i * fontHeight + y);
        }
    }
}
