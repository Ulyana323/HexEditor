package ru.khav.ProjectNIC.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Data
@NoArgsConstructor
public class DataFromFile {

    public DataFromFile( List<Byte> bytes)
    {
        this.bytes=bytes;
    }

    private List<Byte> bytes=new LinkedList<>();

    private List<Integer> bytes10=new LinkedList<>();

    public List<String> getHexFormatOfData() throws IOException
    {
        List<String> lst = new LinkedList<>();
        for (Byte b : this.bytes) {
            lst.add(String.format("%01X ", b));
        }
        //System.out.println(sb.toString());
        return lst;
    }
    public String getDecFormatOfData() throws IOException
    {
        StringBuilder sb = new StringBuilder();
        for (Integer b : this.bytes10) {
            sb.append(b);
            sb.append(" ");
        }
        return sb.toString();
    }

public void clear()
{
    bytes.clear();
    bytes10.clear();
}

}
