package ru.khav.ProjectNIC.utill;

import lombok.Data;
import ru.khav.ProjectNIC.models.DataFromFile;
import ru.khav.ProjectNIC.services.DownloadDataFromFile;

import java.io.*;
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
    public DataFromFile getDataFromFile() throws IOException
    {
        int c;

        try(BufferedReader br = new BufferedReader(new FileReader(file.getPath()))){
            while((c=br.read())!=-1)
            {
                curDataFromFile.getBytes().add((byte)c);
                curDataFromFile.getBytes10().add(c);
            }
        }catch (IOException e)
        {
            System.out.println(e.getMessage());
            throw e;

        }
        return curDataFromFile;
    }

    @Override
    public void updateDataInFile(DataFromFile data) throws IOException{

        try(BufferedOutputStream br=new BufferedOutputStream(new FileOutputStream(file.getPath())))
        {
            byte[] buffer = new byte[data.getBytes().size()];
            for(int i=0;i<buffer.length;i++)
            {
                buffer[i]=data.getBytes().get(i);

            }
           br.write(buffer, 0, buffer.length);
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
