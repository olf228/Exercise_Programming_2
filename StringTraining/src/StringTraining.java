

public class StringTraining {

    public static void enumerateStringChars(String s){
        for (int i = 0; i < s.length(); i++){
            System.out.println(s.charAt(i)+": "+ (i+1));
        }
    }
    public static int countOccurrences(String s, char c){
        int result = 0;
        for (int i = 0; i < s.length(); i++){
            if(s.charAt(i) == c)
                result++;
        }
        return result;
    }
    public static int countLongestSequence(String s){
        int result = 0;
        int counter = 1;
        int temp = 1;
        boolean sequence = false;
        for (int i = 0; i < s.length(); i++){
            if(i == s.length()-1){
                continue;
            }
            else{
                if(s.charAt(i) == s.charAt(i+1)){
                    sequence = true;
                    counter++;
                }
                else{
                    sequence = false;
                    temp = counter;
                    counter = 1;
                }
            }
        }
        if(counter > temp)
            result = counter;
        else result = temp;
        return result;
    }
    public static void main(String args[]){
        StringTraining trainer = new StringTraining();
        trainer.enumerateStringChars("ABCDEF");
        int longestSequence = trainer.countLongestSequence("AAABCCCCCDDDD");
        System.out.println(longestSequence);
        int counter = trainer.countOccurrences("ABCCDDDD", 'C');
        System.out.println(counter);


    }

}

