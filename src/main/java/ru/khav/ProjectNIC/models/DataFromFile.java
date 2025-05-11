package ru.khav.ProjectNIC.models;

import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


@NoArgsConstructor
public class DataFromFile {

    public DataFromFile(List<Byte> bytes, List<Integer> bytes10) {
        this.bytes = bytes;
        this.bytes10 = bytes10;
    }

    private List<Byte> bytes = new LinkedList<>();

    public List<Integer> getBytes10() {
        return bytes10;
    }

    private List<Integer> bytes10 = new LinkedList<>();

    public List<Byte> getBytes() {
        return bytes;
    }

    public List<String> getHexFormatOfData() throws IOException {
        List<String> lst = new LinkedList<>();
        for (int b : this.bytes10) {
            lst.add(String.format("%02X", b));//беззнак вид
        }
        return lst;
    }

    public void clear() {
        bytes.clear();
        bytes10.clear();
    }

}
