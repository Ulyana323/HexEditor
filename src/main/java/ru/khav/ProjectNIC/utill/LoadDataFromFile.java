package ru.khav.ProjectNIC.utill;

import lombok.Data;
import ru.khav.ProjectNIC.models.DataFromFile;
import ru.khav.ProjectNIC.services.DownloadDataFromFile;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class LoadDataFromFile implements DownloadDataFromFile {

    DataFromFile curDataFromFile = new DataFromFile();
    private File file;

    public File getFile() {
        return file;
    }

    public LoadDataFromFile setFile(File file) {
        this.file = file;
        return this;
    }

    @Override
    public DataFromFile getDataByteFromFile() throws IOException {
        curDataFromFile.clear();
        try (DataInputStream dis= new DataInputStream(new FileInputStream(file.getPath()))) {
        while(dis.available()>0){
            byte b=dis.readByte();
            int unsign=b & 0xFF;
            curDataFromFile.getBytes().add(b);
            curDataFromFile.getBytes10().add(unsign);
            }
        } catch (IOException e) {
        System.out.println(e.getMessage());
        throw e;
    }
        return curDataFromFile;
    }

    @Override
    public void updateDataInFile(DataFromFile data) throws IOException{

        try(DataOutputStream dos= new DataOutputStream(new FileOutputStream(file.getPath())))
        {
            byte[] buffer = new byte[data.getBytes().size()];
            for(int i=0;i<buffer.length;i++)
            {
                buffer[i]=data.getBytes().get(i);
            }
          dos.write(buffer, 0, buffer.length);
        }catch (IOException e)
        {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public DataFromFile getCurDataFromFile() {
        return curDataFromFile;
    }
    public void clear()
    {
        this.curDataFromFile.clear();
        this.file=null;
    }
}
