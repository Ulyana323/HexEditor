package ru.khav.ProjectNIC.utill;

import lombok.Data;
import ru.khav.ProjectNIC.models.DataFromFile;
import ru.khav.ProjectNIC.services.DownloadDataFromFile;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

@Data
public class LoadDataFromFile implements DownloadDataFromFile {
    private final DataFromFile curDataFromFile = new DataFromFile();
    private File file;

    private static final int PAGE_SIZE = 200; // 200,
    private long position = 0;
    private long curFileSize;

    @Override
    public  DataFromFile getDataByteFromFile() throws IOException {
        position = 0;
        return readPageAt(position);
    }

    @Override
    public  DataFromFile getNextDataFromFile() throws IOException {
        if (position + PAGE_SIZE >= curFileSize) {
            return curDataFromFile; //последняя стр
        }
        position += PAGE_SIZE;
        return readPageAt(position);
    }
    @Override
    public  DataFromFile getPreviousDataFromFile() throws IOException {
        if (position == 0) {
            return curDataFromFile;//первая стр
        }
        position = Math.max(0, position - PAGE_SIZE);
        return readPageAt(position);
    }

    private synchronized DataFromFile readPageAt(long startPos) throws IOException {
        curDataFromFile.getBytes().clear();
        curDataFromFile.getBytes10().clear();

        try (RandomAccessFile raf = new RandomAccessFile(file, "r");
             FileChannel ch = raf.getChannel()) {

            curFileSize = ch.size();
            long mapSize = Math.min(PAGE_SIZE, curFileSize - startPos);
            MappedByteBuffer in = ch.map(FileChannel.MapMode.READ_ONLY, startPos, mapSize);

            while (in.hasRemaining()) {
                byte b = in.get();
                curDataFromFile.getBytes().add(b);
                curDataFromFile.getBytes10().add(b & 0xFF);
            }
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        return curDataFromFile;
    }

    @Override
    public void updateDataInFile(DataFromFile data) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw");
             FileChannel ch = raf.getChannel()) {

            int dataSize = data.getBytes().size();
            long requiredSize = position + dataSize;

            if (raf.length() < requiredSize) {
                raf.setLength(requiredSize);
            }

            long mapSize = Math.max(PAGE_SIZE, data.getBytes().size());
            MappedByteBuffer out = ch.map(FileChannel.MapMode.READ_WRITE, position, mapSize);

            for (byte b : data.getBytes()) {
                out.put(b);
            }
            out.force();//сбрасываем изменение на диск
        }
    }
    public  void clear()
    {
        curDataFromFile.clear();
    }
    @Override
    public boolean isLastPage()
    {
        return position + PAGE_SIZE >= curFileSize;
    }

}