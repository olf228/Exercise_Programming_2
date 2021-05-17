public class AudioFile {

    /**
     * Variables
     */

    private char osSeparator = java.io.File.separatorChar;
    private String filename = "";
    private String path = "";
    private String author = "";
    private String title = "";

    /**
     * Constructor
     */

    public AudioFile(){}

    public AudioFile(String input){
        parsePathname(input);
        parseFilename(getFilename());
    }

    /**
     * Methods
     */

    // Check, if the operating system is Windows
    public boolean isWindows(){
        return System.getProperty("os.name").toLowerCase().contains("win");
    }
    // Normalizes the pathname into path and filename variable
    public void parsePathname(String input){
        // Check for empty string
        if (input.isEmpty() || input.trim().isEmpty()){
            path = input;
            filename = input;
            return;
        }

        if(input == " "){
            path = " ";
            filename = "";
            return;
        }
        // Normalisation of drive letter
        if (!isWindows() && input.length() >= 1 && input.indexOf(":") == 1){
            input = osSeparator + input.replace(":", "" + osSeparator);
        }
        // Normalisation of separators in path
        input = input.replace('/', osSeparator);
        input = input.replace('\\', osSeparator);
        // Deleting separators next to each other
        while (input.contains(""+osSeparator+osSeparator)){
            input = input.replace(""+osSeparator+osSeparator, "" + osSeparator);
        }

        // Check for single pathname
        int filenameStart, filenameEnd;
        filenameStart = input.lastIndexOf(osSeparator);
        if (filenameStart == -1){
            path = input;
            filename = input;
        }
        else{
            path = input;
            filename = input.substring(filenameStart + 1, input.length());
        }

        // Separate pathname und filename
        int indexOfEnd = input.lastIndexOf(osSeparator);
        filename = input.substring(indexOfEnd + 1, input.length());

    }

    public void parseFilename(String input){
        int endOfAuthor = input.lastIndexOf(" - ");
        if (input.isEmpty()){
            author = input;
            title = input;
        }

        else if (input == "-"){
            author = "";
            title = "-";
        }
        else if (input == " - "){
            author = "";
            title = "";

        }
        else if (input.indexOf(".") == 0){
            author = "";
            title = "";
        }

        else if ((input.length() - 1) > (endOfAuthor + 2) && endOfAuthor > 0){
            author = input.substring(0, endOfAuthor);
            title = input.substring(endOfAuthor + 2, input.lastIndexOf('.'));
        }
        else {
            author = "";
            int indexOfDot = input.indexOf('.');

            if (indexOfDot > -1){
                title = input.substring(0, indexOfDot);
            }
        }
        // Delete unnecessary blanks or tabs at the front and end of the strings
        author = author.trim();
        title = title.trim();


    }

    /**
     * Overrided Methods
     */

    @Override
    public String toString() {
        if (author.isEmpty()) {
            return title;
        } else {
            return author + " - " + title;
        }
    }

    /**
     * Getter
     */

    public String getPathname(){
        return path;
    }

    public String getFilename(){
        return filename;
    }
    public String getAuthor(){
        return author;
    }
    public String getTitle(){
        return title;
    }
}