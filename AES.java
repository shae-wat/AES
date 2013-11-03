import java.util.*;
import java.io.*;
import java.lang.Math;



class AES{

	//normal execution command: java AES option keyFile inputFile 
	//extra credit execution command: java AES option [length] [mode] keyFile inputFile

	public static File keyFile;
	public static File inputFile;
	public static int keysize;

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
        System.out.println("inputFile name = " + fName +"\n\n");

        if (args[0].equals("e")){
            System.out.println("\nargs[0] = " + args[0] + " = encrypt mode\n");
            File encFile = new File(fName+".enc");	
        }
        else {
    		System.out.println("\nargs[0] = " + args[0] + " = decrypt mode");
        	File decFile = new File(fName+".dec");
        }    

		//get keysize from terminal input
		keysize = 128;

		Scanner inputText = new Scanner(new FileReader(inputFile));
		String pt;
		//128-byte block to fill
		byte[][] stateArray = new byte[4][4];

		byte[][]keyArray = {
			{(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00},
			{(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00},
			{(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00},
			{(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00}
		};

		//keySchedule instance
		KeySchedule keySchedule = new KeySchedule(keyArray);
		
		while(inputText.hasNextLine()){
			pt = inputText.nextLine();
			System.out.println("next line of file = " + pt +"\n\n");
			//scanner for this line fo plaintext
			Scanner plaintext = new Scanner(pt);

			//create new instance of stateArray to send thrgh AES
			for(int row = 0; row < 4; row++){
		 		for(int column = 0; column < 4; column++){
		 			if(plaintext.hasNextByte()){
		 				stateArray[row][column] = plaintext.nextByte();
	 				}
	 				//pad to the right with zeros
	 				else{
	 					stateArray[row][column] = 0;
	 				}
		 			System.out.println("stateArray["+row+"]["+column+"] = " + stateArray[row][column]);
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

	        }
	        //Decryption with the newly created stateArray
	        else {
	        	System.out.println("\nargs[0] = " + args[0] + " = decrypt mode");
	            File decFile = new File(fName+".dec");
	            Decode decode = new Decode(stateArray, keyArray);

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
	        }

		}	


		for(int row = 0; row < 4; row++){
			for(int column = 0; column < 4; column++){
				String hexStr = String.format("%x",stateArray[row][column]).toString();
				System.out.print(hexStr + " ");
			}
			System.out.println("");
		}
		System.out.println("stateArray with one line input");



		// for (short[] arr : stateArray) {
  //           System.out.println(Arrays.toString(arr));
  //       }

		// short[][] keyArray = new short[4][4];
		// while (key.hasNextShort()){
		// 	for(int row = 0; row < 4; row++){
		// 		for(int column = 0; column < 4; column++){
		// 			keyArray[row][column] = key.nextShort();

		Scanner key = new Scanner(new FileReader(keyFile));


		



  //       //Encryption creates file with extension ".enc"
		// if (args[0].equals("e")){
  //           System.out.println("\nargs[0] = " + args[0] + " = encrypt mode\n");
  //           File encFile = new File(fName+".enc");	          
  //           Encode encode = new Encode(stateArray, keyArray);

  //           //number of rounds depends on key size
  //           if (keysize == 128){
  //           	//use given cipher key for the initial round
  //           	encode.addRoundKey(0);
  //           	for(int round=1; round < 10; round++){
  //           		System.out.println("-------------------round = " + round);
  //           		encode.subBytes();
  //           		encode.shiftRows();
  //           		encode.mixColumns();
  //           		encode.addRoundKey(round);
  //           	}
  //           	System.out.println("-------------------round = 10");
  //           	encode.subBytes();
  //       		encode.shiftRows();
  //       		encode.addRoundKey(10);
  //           }

  //       }
  //       //Decryption creates file with extension ".dec"
  //       else {
  //       	System.out.println("\nargs[0] = " + args[0] + " = decrypt mode");
  //           File decFile = new File(fName+".dec");
  //           Decode decode = new Decode(invstateArray, keyArray);

  //           //number of rounds depends on key size
  //           if (keysize == 128){
  //           	//use given cipher key for the initial round
  //           	decode.invAddRoundKey(10);
  //           	for(int round=9; round > 0; round--){
  //           		System.out.println("-------------------round = " + round);
		//             decode.invShiftRows();
		//             decode.invSubBytes();
		//             decode.invAddRoundKey(round);
		//             decode.invMixColumns();
  //           	}
  //           	System.out.println("-------------------round = 0");
  //           	decode.invShiftRows();
	 //            decode.invSubBytes();
	 //            decode.invAddRoundKey(0);
  //           }
  //       }

        

		System.out.println("\nAES!");
	}

	
}