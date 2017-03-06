public class Directory extends FileDS
{
    private int[] children;
    private int nrOfChild;

    Directory(String dirName, int position, int parent)
    {
        super(dirName, position, parent);

        this.children = new int[128];
        this.nrOfChild = 0;
    }

    public String getFileName()
    {
        return super.getFileName();
    }

    public void setFileName(String name)
    {
        super.setFileName(name);
    }

    int[] getChild()
    {
        int[] temp = new int[this.nrOfChild];

        System.arraycopy(this.children, 0, temp, 0, this.nrOfChild);

        return temp;
    }

    void removeChildren(int position)
    {
        for (int i = 0; i < this.nrOfChild; i++)
        {
            if (this.children[i] == position)
            {
                this.children[i] = this.children[this.nrOfChild - 1];
                this.nrOfChild--;
            }
        }
    }

    void addChildren(int position)
    {
        this.children[this.nrOfChild] = position;
        this.nrOfChild++;
    }

}