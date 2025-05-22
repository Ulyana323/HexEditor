package ru.khav.ProjectNIC.Controllers;

import lombok.Data;
import ru.khav.ProjectNIC.models.DataFromFile;
import ru.khav.ProjectNIC.Interfaces.ReadDataFromFile;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

@Data
public class LoadDataFromFile implements ReadDataFromFile {
    private static final int PAGE_SIZE = 10 * 1024; // 10кб
    private File file;
    private long position = 0;
    private long curFileSize;


    @Override
    public DataFromFile getDataByteFromFile() throws IOException {
        position = 0;
        return readPageAt(position);
    }

    public DataFromFile getNextDataFromFile() throws IOException {
        if (position + PAGE_SIZE >= curFileSize) {
            return readPageAt(position); //последняя стр
        }
        position += PAGE_SIZE;
        return readPageAt(position);
    }

    public DataFromFile getPreviousDataFromFile() throws IOException {
        if (position == 0) {
            return readPageAt(position); //первая стр
        }
        position = Math.max(0, position - PAGE_SIZE);
        return readPageAt(position);
    }

    private DataFromFile readPageAt(long startPos) throws IOException {
        List<Byte> bytes = new ArrayList<>();
        List<Integer> bytes10 = new ArrayList<>();

        try (RandomAccessFile raf = new RandomAccessFile(file, "r");
             FileChannel ch = raf.getChannel()) {

            curFileSize = ch.size();
            long mapSize = Math.min(PAGE_SIZE, curFileSize - startPos);
            MappedByteBuffer in = ch.map(FileChannel.MapMode.READ_ONLY, startPos, mapSize);

            while (in.hasRemaining()) {
                byte b = in.get();
                bytes.add(b);
                bytes10.add(b & 0xFF);
            }
        }

        return new DataFromFile(bytes, bytes10);
    }

    @Override
    public void updateDataInFile(DataFromFile data) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw");
             FileChannel ch = raf.getChannel()) {

            long mapSize = Math.min(PAGE_SIZE, data.getBytes().size());
            MappedByteBuffer out = ch.map(FileChannel.MapMode.READ_WRITE, position, mapSize);

            for (byte b : data.getBytes()) {
                out.put(b);
            }
        }
    }

    @Override
    public boolean isLastPage() {
        return position + PAGE_SIZE >= curFileSize;
    }


}