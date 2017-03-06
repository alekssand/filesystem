import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Filesystem
{

    private FileDS[] myfileArray;
    private int pwd;

    public Filesystem()
    {
        this.myfileArray = new FileDS[128];
    }

    public String format()
    {

        for (int i = 0; i < myfileArray.length; i++)
        {
            myfileArray[i] = null;
        }

        myfileArray[0] = new Directory("/", 0, 0);
        this.pwd = 0;

        return "Diskformat Successful";
    }

    public String ls(String[] p_asPath)
    {
        System.out.print("Listing directory ");

        int[] temp;
        Directory tempDirectory = (Directory) this.myfileArray[this.pwd];

        temp = tempDirectory.getChild();

        for (int aTemp : temp)
        {
            System.out.print("\n" + this.myfileArray[aTemp].getFileName());
        }

        return "";
    }

    public String create(String[] p_asPath, byte[] p_abContents)
    {
        System.out.print("Creating file ");

        int curPwd = this.pwd;

        for (int i = 0; i < p_asPath.length - 1; i++)
        {

            int[] tempChildren = ((Directory) this.myfileArray[curPwd]).getChild();

            for (int aTempChildren : tempChildren)
            {
                if (this.myfileArray[aTempChildren].getFileName().compareTo(p_asPath[i]) == 0)
                {
                    curPwd = aTempChildren;
                }
            }
        }

        int n = 0;

        while (this.myfileArray[n] != null)
        {
            n++;
        }

        String tempString = new String(p_abContents);
        File file = new File(p_asPath[p_asPath.length - 1], n, curPwd, tempString);
        this.myfileArray[n] = file;
        ((Directory) this.myfileArray[curPwd]).addChildren(n);

        return "";
    }

    public String cat(String[] p_asPath)
    {
        System.out.print("Dumping contents of file \n");

        int curPwd = this.pwd;

        for (String aP_asPath : p_asPath)
        {
            int[] temp = ((Directory) this.myfileArray[curPwd]).getChild();

            for (int aTemp : temp)
            {
                if (this.myfileArray[aTemp].getFileName().compareTo(aP_asPath) == 0 && this.myfileArray[aTemp] instanceof Directory)
                {
                    curPwd = aTemp;
                }
                else if (this.myfileArray[aTemp].getFileName().compareTo(aP_asPath) == 0 && this.myfileArray[aTemp] instanceof File)
                {
                    System.out.println(((File) this.myfileArray[aTemp]).getFileContent());
                }
            }
        }
        return "";
    }

    public String save(String p_sPath)
    {
        System.out.print("Saving filesystem to file " + p_sPath);

        try
        {
            BufferedWriter output = new BufferedWriter(new FileWriter(p_sPath));

            int nrOfFiles = 0;

            while (this.myfileArray[nrOfFiles] != null)
            {
                nrOfFiles++;
            }

            output.write(nrOfFiles + "\n");

            for (int i = 0; i < nrOfFiles; i++)
            {
                if (this.myfileArray[i] instanceof Directory)
                {
                    output.write("Directory\n");
                    output.write((this.myfileArray[i]).getFileName() + "\n");
                    output.write((this.myfileArray[i]).getFileParent() + "\n");
                    output.write((this.myfileArray[i]).getFilePosition() + "\n");

                    int[] temp = ((Directory) this.myfileArray[i]).getChild();

                    for (int aTemp : temp)
                    {
                        output.write(aTemp + " ");
                    }
                    output.write("\n");
                }
                else
                {
                    output.write("File\n");
                    output.write(((File) this.myfileArray[i]).getFileName() + "\n");
                    output.write(((File) this.myfileArray[i]).getFileParent() + "\n");
                    output.write(((File) this.myfileArray[i]).getFilePosition() + "\n");
                    output.write(((File) this.myfileArray[i]).getFileContent() + "\n");
                    output.write("EOF\n");
                }
            }

            output.close();

        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return "";
    }

    public String read(String p_sPath)
    {
        System.out.print("Reading file " + p_sPath + " to filesystem");

        for (int i = 0; i < this.myfileArray.length; i++)
        {
            this.myfileArray[i] = null;
        }

        try
        {
            BufferedReader in = new BufferedReader(new FileReader(p_sPath));
            String readLine= "";

            int i = 0;
            readLine = in.readLine();
            while (readLine != null)
            {

                if (readLine.equals("Directory"))
                {
                    String name = in.readLine();
                    int parent = Integer.parseInt(in.readLine());
                    int pos = Integer.parseInt(in.readLine());
                    readLine = in.readLine();

                    String[] childrenString = readLine.split(" ");
                    int[] children = new int[childrenString.length];

                    for (int n = 0; n < children.length; n++)
                    {
                        children[n] = Integer.parseInt(childrenString[n]);
                    }

                    this.myfileArray[i] = new Directory(name, pos, parent);

                    for (int aChildren : children)
                    {
                        ((Directory) this.myfileArray[i]).addChildren(aChildren);
                    }
                    i++;
                }
                else
                {
                    String fileName = in.readLine();
                    int fileParent = Integer.parseInt(in.readLine());
                    int filePos = Integer.parseInt(in.readLine());
                    String fileContent = "";

                    readLine = in.readLine();
                    while (!readLine.equals("EOF"))
                    {
                        fileContent = fileContent.concat(readLine);
                        readLine = in.readLine();
                    }
                    this.myfileArray[i] = new File(fileName, filePos, fileParent, fileContent);
                    i++;
                }
                readLine = in.readLine();
            }
            in.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return "";
    }

    public String rm(String[] p_asPath)
    {
        System.out.print("Removing file ");

        int currentPwd = this.pwd;

        for (String aP_asPath : p_asPath)
        {
            int[] children = ((Directory) this.myfileArray[currentPwd]).getChild();

            for (int aChildren : children)
            {
                if (this.myfileArray[aChildren].getFileName().compareTo(aP_asPath) == 0 && this.myfileArray[aChildren] instanceof Directory)
                {
                    currentPwd = aChildren;
                }
                else if (this.myfileArray[aChildren].getFileName().compareTo(aP_asPath) == 0 && this.myfileArray[aChildren] instanceof File)
                {
                    ((Directory) this.myfileArray[currentPwd]).removeChildren(aChildren);
                }
            }
        }

        return "";
    }

    public String copy(String[] p_asSource, String[] p_asDestination)
    {
        System.out.print("Copying file \n");

        int currentPwd = this.pwd;

        for (String aP_asSource : p_asSource)
        {
            int[] tempChildren = ((Directory) this.myfileArray[currentPwd]).getChild();

            for (int aTempChildren : tempChildren)
            {
                if (this.myfileArray[aTempChildren].getFileName().compareTo(aP_asSource) == 0 && this.myfileArray[aTempChildren] instanceof Directory)
                {
                    currentPwd = aTempChildren;
                }
                else if (this.myfileArray[aTempChildren].getFileName().compareTo(aP_asSource) == 0 && this.myfileArray[aTempChildren] instanceof File)
                {
                    this.create(p_asDestination, ((File) this.myfileArray[aTempChildren]).getFileContent().getBytes());
                }
            }
        }

        return "";
    }

    public String append(String[] p_asSource, String[] p_asDestination)
    {
        System.out.print("Appending file ");

        int currentPwd = this.pwd;
        String textToAppend = "";

        for (String aP_asSource : p_asSource)
        {
            for (int n = 0; n < ((Directory) myfileArray[currentPwd]).getChild().length; n++)
            {
                int[] temp = ((Directory) myfileArray[currentPwd]).getChild();
                if (this.myfileArray[temp[n]].getFileName().compareTo(aP_asSource) == 0 && this.myfileArray[temp[n]] instanceof Directory)
                {
                    currentPwd = temp[n];
                }
                else if (this.myfileArray[temp[n]].getFileName().compareTo(aP_asSource) == 0 && this.myfileArray[temp[n]] instanceof File)
                {
                    textToAppend = ((File) this.myfileArray[temp[n]]).getFileContent();
                }
            }
        }

        currentPwd = this.pwd;

        for (String aP_asDestination : p_asDestination)
        {
            for (int n = 0; n < ((Directory) myfileArray[currentPwd]).getChild().length; n++)
            {
                int[] temp = ((Directory) myfileArray[currentPwd]).getChild();
                if (this.myfileArray[temp[n]].getFileName().compareTo(aP_asDestination) == 0 && this.myfileArray[temp[n]] instanceof Directory)
                {
                    currentPwd = temp[n];
                }
                else if (this.myfileArray[temp[n]].getFileName().compareTo(aP_asDestination) == 0 && this.myfileArray[temp[n]] instanceof File)
                {
                    String temp1 = ((File) this.myfileArray[temp[n]]).getFileContent();
                    textToAppend = textToAppend.concat(temp1);
                    ((File) this.myfileArray[temp[n]]).setFileContent(textToAppend);
                }
            }
        }

        return "";
    }

    public String rename(String[] p_asSource, String[] p_asDestination)
    {
        System.out.print("Renaming file ");

        int currentPwd = this.pwd;

        for (String aP_asSource : p_asSource)
        {

            boolean found = false;
            for (int n = 0; n < ((Directory) myfileArray[currentPwd]).getChild().length && !found; n++)
            {
                int[] temp = ((Directory) myfileArray[currentPwd]).getChild();

                if (this.myfileArray[temp[n]].getFileName().compareTo(aP_asSource) == 0 && this.myfileArray[temp[n]] instanceof Directory)
                {
                    currentPwd = temp[n];
                    found = true;
                }
                else if (this.myfileArray[temp[n]].getFileName().compareTo(aP_asSource) == 0 && this.myfileArray[temp[n]] instanceof File)
                {
                    this.myfileArray[temp[n]].setFileName(p_asDestination[0]);
                    found = true;
                }
            }

        }

        return "";
    }

    public String mkdir(String[] p_asPath)
    {
        System.out.print("Creating directory ");

        int currentPwd = this.pwd;

        for (String aP_asPath : p_asPath)
        {
            int n = 0;
            while (myfileArray[n] != null)
            {
                n++;
            }

            myfileArray[n] = new Directory(aP_asPath, n, currentPwd);
            ((Directory) myfileArray[currentPwd]).addChildren(n);

            currentPwd = n;
        }

        return "";
    }

    public String cd(String[] p_asPath)
    {
        System.out.print("Changing directory to ");
        int tempPwd = this.pwd;

        for (String aP_asPath : p_asPath)
        {

            switch (aP_asPath)
            {
                case "..":
                    tempPwd = this.myfileArray[tempPwd].getFileParent();
                    break;
                case ".":

                    break;
                default:
                    int[] temp = ((Directory) this.myfileArray[tempPwd]).getChild();

                    for (int aTemp : temp)
                    {
                        String tempString = this.myfileArray[aTemp].getFileName();
                        if (tempString.compareTo(aP_asPath) == 0)
                        {
                            tempPwd = aTemp;
                        }
                    }
                    break;
            }
            this.pwd = tempPwd;
        }
        return this.myfileArray[this.pwd].getFileName();
    }

    public String pwd()
    {
        if (this.myfileArray[this.pwd] == null)
        {
            return "/unknown/";
        }
        else
        {
            return this.myfileArray[this.pwd].getFileName();
        }
    }

    private void dumpArray(String[] p_asArray)
    {
        for (String aP_asArray : p_asArray)
        {
            System.out.print(aP_asArray + "=>");
        }
    }
}