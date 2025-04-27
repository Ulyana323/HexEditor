package ru.khav.ProjectNIC.utill;

import ru.khav.ProjectNIC.models.DataFromFile;
import ru.khav.ProjectNIC.services.DownloadDataFromFile;

import java.io.*;
public class LoadDataFromFile implements DownloadDataFromFile {

    DataFromFile dataFromFile = new DataFromFile();

    public File getFile() {
        return file;
    }

    public LoadDataFromFile setFile(File file) {
        this.file = file;
        return this;
    }

    private File file;

    @Override
    public DataFromFile getDataFromFile() throws IOException
    {
        int c;

        try(BufferedReader br = new BufferedReader(new FileReader(file.getPath()))){
            while((c=br.read())!=-1)
            {
                dataFromFile.getBytes().add((byte)c);
                dataFromFile.getBytes10().add(c);
            }
        }catch (IOException e)
        {
            System.out.println(e.getMessage());
            throw e;

        }
        return dataFromFile;
    }

    @Override
    public int updateDataInFile(DataFromFile data) {
        return 0;
    }

    public static void main(String[] args)  {

        try {
            File file1 = new File("D:\\JAVA\\HexEditor\\src\\main\\java\\ru\\khav\\ProjectNIC\\hello.txt");
            LoadDataFromFile loadDataFromFile = new LoadDataFromFile();
            loadDataFromFile.setFile(file1);
        DataFromFile df= loadDataFromFile.getDataFromFile();
            System.out.println(df.getHexFormatOfData());
         System.out.println(df.getDecFormatOfData());

        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }

    }
}
