package kr.co.paycast.utils;

import java.util.Random;

@SuppressWarnings("unchecked")
public class GenerateCert {
	private int certNumLength = 6;
    private final char[] characterTable = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 
            'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };
    
    public String excuteGenerateNumber() {
        Random random = new Random( System.currentTimeMillis() );
        
        int range = (int)Math.pow(10, certNumLength);
        int trim = (int)Math.pow(10, certNumLength-1);
        int result = random.nextInt(range)+trim;
        if( result>range ){
            result = result-trim;
        }
        
        return String.valueOf(result);
    }
    
    public String excuteGenerateCharacter() {
        Random random = new Random(System.currentTimeMillis());
        int tablelength = characterTable.length;
        StringBuffer buf = new StringBuffer();
        
        for(int i = 0; i < certNumLength; i++) {
            buf.append(characterTable[random.nextInt(tablelength)]);
        }
        
        return buf.toString();
    }
    
    public int getCertNumLength() {
        return certNumLength;
    }

    public void setCertNumLength(int certNumLength) {
        this.certNumLength = certNumLength;
    }
}
