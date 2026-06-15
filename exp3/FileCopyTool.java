import java.io.IOException;
import java.util.Scanner;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileCopyTool {
    private static final long CHUNK_SIZE = 16L * 1024 * 1024;

    public static void main(String[] args) {
        Path sourceFile;
        Path targetDirectory;

        if (args.length == 2) {
            sourceFile = Paths.get(args[0]).toAbsolutePath().normalize();
            targetDirectory = Paths.get(args[1]).toAbsolutePath().normalize();
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.println("请输入源文件路径:");
            String sourceInput = scanner.nextLine().trim();
            System.out.println("请输入目标文件夹路径:");
            String targetInput = scanner.nextLine().trim();

            if (sourceInput.isEmpty() || targetInput.isEmpty()) {
                System.err.println("输入不能为空。");
                printUsage();
                return;
            }

            sourceFile = Paths.get(sourceInput).toAbsolutePath().normalize();
            targetDirectory = Paths.get(targetInput).toAbsolutePath().normalize();
        }

        try {
            copyFile(sourceFile, targetDirectory);
        } catch (IOException e) {
            System.err.println("复制失败: " + e.getMessage());
        }
    }

    private static void copyFile(Path sourceFile, Path targetDirectory) throws IOException {
        if (!Files.exists(sourceFile)) {
            throw new IOException("源文件不存在: " + sourceFile);
        }
        if (!Files.isRegularFile(sourceFile)) {
            throw new IOException("源路径不是文件: " + sourceFile);
        }

        Files.createDirectories(targetDirectory);
        Path targetFile = targetDirectory.resolve(sourceFile.getFileName());

        long fileSize = Files.size(sourceFile);
        long startTime = System.nanoTime();

        try (FileChannel sourceChannel = FileChannel.open(sourceFile, StandardOpenOption.READ);
             FileChannel targetChannel = FileChannel.open(
                     targetFile,
                     StandardOpenOption.CREATE,
                     StandardOpenOption.WRITE,
                     StandardOpenOption.TRUNCATE_EXISTING)) {

            long position = 0;
            while (position < fileSize) {
                long transferred = sourceChannel.transferTo(
                        position,
                        Math.min(CHUNK_SIZE, fileSize - position),
                        targetChannel
                );

                if (transferred <= 0) {
                    throw new IOException("复制过程中未能继续传输数据。");
                }
                position += transferred;
            }
        }

        long elapsedNanos = System.nanoTime() - startTime;
        double elapsedSeconds = elapsedNanos / 1_000_000_000.0;
        double speedMbPerSecond = elapsedSeconds > 0
                ? fileSize / 1024.0 / 1024.0 / elapsedSeconds
                : 0.0;

        System.out.println("复制成功");
        System.out.println("源文件: " + sourceFile);
        System.out.println("目标文件: " + targetFile);
        System.out.printf("文件大小: %,d 字节%n", fileSize);
        System.out.printf("耗时: %.3f 秒%n", elapsedSeconds);
        System.out.printf("平均速度: %.2f MB/s%n", speedMbPerSecond);
    }

    private static void printUsage() {
        System.out.println("可直接运行后按提示输入路径，或使用命令行参数。");
        System.out.println("命令行用法:");
        System.out.println("java FileCopyTool <源文件路径> <目标文件夹路径>");
        System.out.println("示例:");
        System.out.println("java FileCopyTool D:\\data\\a.zip D:\\backup");
    }
}
