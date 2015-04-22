import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import java.math.BigInteger;
import java.util.UUID;

/**
 * Created by vcarvalho on 4/22/15.
 */
public class UUIDTest {

    @Test
    public void littleEndian(){
        BigInteger low = new BigInteger("10685381227293465328");
        BigInteger high = new BigInteger("2549528470496332196");

        byte[] lowBytes = reverse(low.toByteArray());
        byte[] highBytes = reverse(high.toByteArray());
        byte[] data1 = new byte[lowBytes.length+highBytes.length];
        System.arraycopy(lowBytes,0,data1,0,lowBytes.length);
        System.arraycopy(highBytes, 0, data1, lowBytes.length, highBytes.length);

        StringBuilder builder = new StringBuilder(Hex.encodeHexString(data1));
        builder.insert(8,"-").insert(13,"-").insert(18,"-").insert(23,"-");
        System.out.println(builder);
    }


    public byte[] reverse(byte[] data){
        byte[] reversed = new byte[8];
        for(int i=0;i<reversed.length;i++){
            reversed[i] = data[data.length-1-i];
        }
        return reversed;
    }

}
