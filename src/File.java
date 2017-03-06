class File extends FileDS
{
    private String content;

    File(String name, int pos, int parent, String content)
    {
        super(name, pos, parent);

        this.setFileContent(content);
    }


    void setFileContent(String content)
    {
        this.content = content;
    }

    String getFileContent()
    {
        return this.content;
    }
}