import java.util.*;
import java.io.*;
import java.lang.Math;



class AES{

	//normal execution command: java AES option keyFile inputFile 
	//extra credit execution command: java AES option [length] [mode] keyFile inputFile

	public static File keyFile;
	public static File inputFile;
	public static int keysize;
	public static BufferedWriter encBw;
	public static BufferedWriter decBw;

	public static void main(String[] args) throws IOException{

		keyFile = new File(args[1]);
        System.out.println("args[1] = " + args[1]);
        inputFile = new File(args[2]);
        System.out.println("args[2] = " + args[2]);

        //Get name of inputFile as a string without any extensions
        String fName = inputFile.getName();
        int pos = fName.lastIndexOf(".");
        if (pos > 0) {
            fName = fName.substring(0, pos);
        }
        System.out.println("inputFile name = " + fName);

        if (args[0].equals("e")){
        	System.out.println("\nargs[0] = " + args[0] + " = encrypt mode");
            File encFile = new File(fName+".enc");	
            //file writing mechanisms to write encoded lines to new .enc file
            FileWriter encFw = new FileWriter(encFile);
            encBw = new BufferedWriter(encFw);
        }
        else {
        	System.out.println("\nargs[0] = " + args[0] + " = decrypt mode");
        	File decFile = new File(fName+".dec");
        	FileWriter decFw = new FileWriter(decFile);
            decBw = new BufferedWriter(decFw);
        }    

		//get keysize from terminal input
		keysize = 128;

		Scanner key = new Scanner(new FileReader(keyFile));
		String line;
		byte[][]keyArray = new byte[4][4];;
		if(key.hasNextLine()){
			line = key.nextLine();
			//System.out.println("\n\nline of keyfile = " + line +"\n");
			
			int counter = 0;
			for(int row = 0; row < 4; row++){
				for(int column = 0; column < 4; column++){
					char val1 = line.charAt(counter);
					char val2 = line.charAt(counter + 1);
					String strByte = val1 + "" + val2;
					keyArray[row][column] = (byte) (Integer.parseInt(strByte,16));
					counter += 2;
				}
			}

		}

		//keySchedule instance from given key
		KeySchedule keySchedule = new KeySchedule(keyArray);
		
		Scanner inputText = new Scanner(new FileReader(inputFile));
		//128-byte block to fill
		byte[][] stateArray = new byte[4][4];

		while(inputText.hasNextLine()){
			line = inputText.nextLine();
			System.out.println("\nline of input file = " + line +"\n");
			if(line.length() == 32){
			
				int counter = 0;
				for(int row = 0; row < 4; row++){
					for(int column = 0; column < 4; column++){
						char val1 = line.charAt(counter);
						char val2 = line.charAt(counter + 1);
						String strByte = val1 + "" + val2;
						stateArray[row][column] = (byte) (Integer.parseInt(strByte,16));
						counter += 2;
					}
				}

			 	//Encryption with the newly created stateArray
				if (args[0].equals("e")){          
		            Encode encode = new Encode(stateArray, keySchedule);

		            //number of rounds depends on key size
		            if (keysize == 128){
		            	//use given cipher key for the initial round
		            	encode.addRoundKey(0);
		            	for(int round=1; round < 10; round++){
		            		System.out.println("-------------------round = " + round);
		            		encode.subBytes();
		            		encode.shiftRows();
		            		encode.mixColumns();
		            		encode.addRoundKey(round);
		            	}
		            	System.out.println("-------------------round = 10");
		            	encode.subBytes();
		        		encode.shiftRows();
		        		encode.addRoundKey(10);
		            }
		            //write encoded version of input line to .enc file
		            for(int row = 0; row < 4; row++){
						for(int column = 0; column < 4; column++){
							String hexStr = String.format("%x",stateArray[row][column]).toString();
							//pad front of hex with 0
							if (hexStr.length() == 1){
								hexStr = "0" + hexStr;
							}
							decBw.write(hexStr);
						}
					}
		            encBw.write("\n");
		      

		        }
		        //Decryption with the newly created stateArray
		        else {
		            Decode decode = new Decode(stateArray, keySchedule);

		            //number of rounds depends on key size
		            if (keysize == 128){
		            	//use given cipher key for the initial round
		            	decode.invAddRoundKey(10);
		            	for(int round=9; round > 0; round--){
		            		System.out.println("-------------------round = " + round);
				            decode.invShiftRows();
				            decode.invSubBytes();
				            decode.invAddRoundKey(round);
				            decode.invMixColumns();
		            	}
		            	System.out.println("-------------------round = 0");
		            	decode.invShiftRows();
			            decode.invSubBytes();
			            decode.invAddRoundKey(0);
		            }
		            //write encoded version of input line to .enc file
		            for(int row = 0; row < 4; row++){
						for(int column = 0; column < 4; column++){
							String hexStr = String.format("%x",stateArray[row][column]).toString();
							//pad front of hex with 0
							if (hexStr.length() == 1){
								hexStr = "0" + hexStr;
							}
							decBw.write(hexStr);
						}
					}
		            decBw.write("\n");
		        }
	        }
	        

			//System.out.println("\nAES!");
		}
		if (args[0].equals("e")){ 
        	encBw.flush();
            encBw.close();
        }
        else{
        	decBw.flush();
            decBw.close();
        }
	}

	
}