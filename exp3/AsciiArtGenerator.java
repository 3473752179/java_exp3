import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class AsciiArtGenerator {
    private static final String CHARSET =
            "$@B%8&WM#*oahkbdpqwmZO0QLCJUYXzcvunxrjft/\\|()1{}[]?-_+~<>i!lI;:,\"^`'. ";
    private static final int DEFAULT_WIDTH = 100;
    private static final double FONT_RATIO = 0.55;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("请输入图片路径:");
        String imageInput = normalizeInput(scanner.nextLine());
        System.out.println("请输入输出文本文件路径:");
        String outputInput = normalizeInput(scanner.nextLine());
        System.out.println("请输入字符画宽度(直接回车使用默认值 100):");
        String widthInput = normalizeInput(scanner.nextLine());

        if (imageInput.isEmpty() || outputInput.isEmpty()) {
            System.err.println("图片路径和输出路径不能为空。");
            return;
        }

        int outputWidth = parseWidth(widthInput);
        Path imagePath = Paths.get(imageInput).toAbsolutePath().normalize();
        Path outputPath = Paths.get(outputInput).toAbsolutePath().normalize();

        try {
            convertToAsciiFile(imagePath, outputPath, outputWidth);
            System.out.println("转换成功");
            System.out.println("图片路径: " + imagePath);
            System.out.println("输出文件: " + outputPath);
            System.out.println("字符画宽度: " + outputWidth);
        } catch (IOException e) {
            System.err.println("转换失败: " + e.getMessage());
        }
    }

    private static void convertToAsciiFile(Path imagePath, Path outputPath, int outputWidth) throws IOException {
        if (!Files.exists(imagePath)) {
            throw new IOException("图片文件不存在: " + imagePath);
        }
        if (!Files.isRegularFile(imagePath)) {
            throw new IOException("输入路径不是文件: " + imagePath);
        }

        BufferedImage image = ImageIO.read(imagePath.toFile());
        if (image == null) {
            throw new IOException("无法识别图片格式，请使用常见图片格式。");
        }

        String asciiArt = convertImageToAscii(image, outputWidth);
        if (outputPath.getParent() != null) {
            Files.createDirectories(outputPath.getParent());
        }
        Files.writeString(outputPath, asciiArt, StandardCharsets.UTF_8);
    }

    private static String convertImageToAscii(BufferedImage image, int outputWidth) {
        int sourceWidth = image.getWidth();
        int sourceHeight = image.getHeight();
        int outputHeight = Math.max(1, (int) Math.round(sourceHeight * outputWidth * FONT_RATIO / sourceWidth));
        double[][] brightnessGrid = new double[outputHeight][outputWidth];
        double minBrightness = Double.MAX_VALUE;
        double maxBrightness = Double.MIN_VALUE;

        for (int y = 0; y < outputHeight; y++) {
            int startY = y * sourceHeight / outputHeight;
            int endY = Math.max(startY + 1, (y + 1) * sourceHeight / outputHeight);
            for (int x = 0; x < outputWidth; x++) {
                int startX = x * sourceWidth / outputWidth;
                int endX = Math.max(startX + 1, (x + 1) * sourceWidth / outputWidth);
                double brightness = getAverageBrightness(image, startX, endX, startY, endY);
                brightnessGrid[y][x] = brightness;
                minBrightness = Math.min(minBrightness, brightness);
                maxBrightness = Math.max(maxBrightness, brightness);
            }
        }

        double range = Math.max(1.0, maxBrightness - minBrightness);

        StringBuilder builder = new StringBuilder();

        for (int y = 0; y < outputHeight; y++) {
            for (int x = 0; x < outputWidth; x++) {
                double normalizedBrightness = (brightnessGrid[y][x] - minBrightness) / range;
                normalizedBrightness = Math.pow(normalizedBrightness, 0.9);
                builder.append(mapBrightnessToChar(normalizedBrightness));
            }
            builder.append(System.lineSeparator());
        }

        return builder.toString();
    }

    private static double getAverageBrightness(
            BufferedImage image,
            int startX,
            int endX,
            int startY,
            int endY
    ) {
        double totalBrightness = 0.0;
        int pixelCount = 0;

        for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++) {
                totalBrightness += getBrightness(image.getRGB(x, y));
                pixelCount++;
            }
        }

        return pixelCount == 0 ? 255.0 : totalBrightness / pixelCount;
    }

    private static double getBrightness(int rgb) {
        int alpha = (rgb >>> 24) & 0xFF;
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;

        double opacity = alpha / 255.0;
        red = (int) Math.round(red * opacity + 255 * (1 - opacity));
        green = (int) Math.round(green * opacity + 255 * (1 - opacity));
        blue = (int) Math.round(blue * opacity + 255 * (1 - opacity));

        return 0.299 * red + 0.587 * green + 0.114 * blue;
    }

    private static char mapBrightnessToChar(double normalizedBrightness) {
        int index = (int) Math.round(normalizedBrightness * (CHARSET.length() - 1));
        return CHARSET.charAt(index);
    }

    private static int parseWidth(String widthInput) {
        if (widthInput.isEmpty()) {
            return DEFAULT_WIDTH;
        }

        try {
            int width = Integer.parseInt(widthInput);
            return width > 0 ? width : DEFAULT_WIDTH;
        } catch (NumberFormatException e) {
            return DEFAULT_WIDTH;
        }
    }

    private static String normalizeInput(String input) {
        String value = input == null ? "" : input.trim();
        if (value.length() >= 2 && value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1).trim();
        }
        return value;
    }
}
