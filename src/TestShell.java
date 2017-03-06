public class TestShell
{
    public static void main(String[] args)
    {
        Filesystem FS = new Filesystem();
        Shell Bash = new Shell(FS, null);
        Bash.start();
    }
}