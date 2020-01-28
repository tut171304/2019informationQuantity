package s4.B171304; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID. 
import java.lang.*;
import s4.specification.*;

/* What is imported from s4.specification
package s4.specification;
public interface InformationEstimatorInterface{
    void setTarget(byte target[]); // set the data for computing the information quantities
    void setSpace(byte space[]); // set data for sample space to computer probability
    double estimation(); // It returns 0.0 when the target is not set or Target's length is zero;
// It returns Double.MAX_VALUE, when the true value is infinite, or space is not set.
// The behavior is undefined, if the true value is finete but larger than Double.MAX_VALUE.
// Note that this happens only when the space is unreasonably large. We will encounter other problem anyway.
// Otherwise, estimation of information quantity, 
}                        
*/
/*計算するときにIq(a)もIq(ab)もIq(abc)もすべて必要
必要なintの情報は先頭から何文字かという情報のみ　それをdouble配列に渡す
最初に計算して記憶しておき、後は取り出すだけ*/ 

public class InformationEstimator implements InformationEstimatorInterface{
    // Code to tet, *warning: This code condtains intentional problem*
    byte [] myTarget; // data to compute its information quantity
    byte [] mySpace;  // Sample space to compute the probability
    FrequencerInterface myFrequencer;  // Object for counting frequency
	double [] dp = null;		//計算しておいたIqを格納しておく配列

    byte [] subBytes(byte [] x, int start, int end) {
	// corresponding to substring of String for  byte[] ,
	// It is not implement in class library because internal structure of byte[] requires copy.
	byte [] result = new byte[end - start];
	for(int i = 0; i<end - start; i++) { result[i] = x[start + i]; };
	return result;
    }

    // IQ: information quantity for a count,  -log2(count/sizeof(space))
    double iq(int freq) {
	return  - Math.log10((double) freq / (double) mySpace.length)/ Math.log10((double) 2.0);
    }

    public void setTarget(byte [] target) 
	{
		myTarget = target;
		myFrequencer.setTarget(target);
		dp = null;
	}
    public void setSpace(byte []space) { 
	myFrequencer = new Frequencer();
	mySpace = space; myFrequencer.setSpace(space); 
    }
/*
	public double min(double [] d){
		double min = d[0];
		for(int i = 1; i < d.length; i++){
			if(min > d[i]){
				min = d[i];
			}
		}
		return min;
		
	}*/
	

    public double estimation(){
		if (dp == null) {
			dp = new double[myTarget.length];
			for(int i = 1; i <= dp.length; i++){	
				double min = iq(myFrequencer.subByteFrequency(0, i));
				for (int k = 1; k < i; k++) {
					double tmp = dp[k -1] + iq(myFrequencer.subByteFrequency(k, i));
					if (min > tmp) {
						min = tmp;
					}
				}
				dp[i - 1] = min;
				/*
				dp[i] = min(
					iq(myFrequencer.subByteFrequency(0, i)),
					dp[1] + iq(1, i),
					dp[2] + iq(2, i),
					dp[3] + iq(3, i),
					...,
					dp[myTarget.length - 1] + iq(myTarget.length - 1, i));
				*/
			}
		}
		return dp[myTarget.length - 1];

	/*boolean [] partition = new boolean[myTarget.length+1];
	int np;
	np = 1<<(myTarget.length-1);
	// System.out.println("np="+np+" length="+myTarget.length);
	double value = Double.MAX_VALUE; // value = mininimum of each "value1".

	for(int p=0; p<np; p++) { // There are 2^(n-1) kinds of partitions.
	    // binary representation of p forms partition.
	    // for partition {"ab" "cde" "fg"}
	    // a b c d e f g   : myTarget
	    // T F T F F T F T : partition:
	    partition[0] = true; // I know that this is not needed, but..
	    for(int i=0; i<myTarget.length -1;i++) {
		partition[i+1] = (0 !=((1<<i) & p));
	    }
	    partition[myTarget.length] = true;

	    // Compute Information Quantity for the partition, in "value1"
	    // value1 = IQ(#"ab")+IQ(#"cde")+IQ(#"fg") for the above example
            double value1 = (double) 0.0;
	    int end = 0;;
	    int start = end;
	    while(start<myTarget.length) {
		// System.out.write(myTarget[end]);
		end++;;
		while(partition[end] == false) { 
		    // System.out.write(myTarget[end]);
		    end++;
		}
		// System.out.print("("+start+","+end+")");
		myFrequencer.setTarget(subBytes(myTarget, start, end));
		value1 = value1 + iq(myFrequencer.frequency());
		start = end;
	    }
	    // System.out.println(" "+ value1);

	    // Get the minimal value in "value"
	    if(value1 < value) value = value1;
	}
	return value;*/
    }

    public static void main(String[] args) {
	InformationEstimator myObject;
	double value;
	myObject = new InformationEstimator();
	myObject.setSpace("3210321001230123".getBytes());
	myObject.setTarget("0".getBytes());
	value = myObject.estimation();
	System.out.println(">0 "+value);
	myObject.setTarget("01".getBytes());
	value = myObject.estimation();
	System.out.println(">01 "+value);
	myObject.setTarget("0123".getBytes());
	value = myObject.estimation();
	System.out.println(">0123 "+value);
	myObject.setTarget("00".getBytes());
	value = myObject.estimation();
	System.out.println(">00 "+value);
    }
}
				  
			       

	
    
