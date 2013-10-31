import java.util.*;
import java.io.*;
import java.lang.Math;



class AES{

	//normal execution command: java AES option keyFile inputFile 
	//extra credit execution command: java AES option [length] [mode] keyFile inputFile

	public static File keyFile;
	public static File inputFile;
	public static int keysize;

	//stuff from prev assignment
        //Encode encodeFile = new Encode(testText, encTestText, huffman, true);
        //Decode decodeFile = new Decode(encTestText, decTestText, newTree, huffman, tree, doubleCharMap, true);
		//BufferedWriter bw = new BufferedWriter(fw);
    	//bWarray[0] = bw;

	public static void main(String[] args) throws IOException{

		// //read the keyfile
		// while (keyFile.hasNext()){
		// 	freqOfAlpha = Integer.valueOf(keyFile.nextLine());
		// }

		// read the whole file as a string ??????????????????
		// String content = new Scanner(new File("filename")).useDelimiter("\\Z").next();

		keyFile = new File(args[1]);
        System.out.println("args[1] = " + args[1]);
        inputFile = new File(args[2]);
        System.out.println("args[2] = " + args[2]);

        //Get name of inputFile as a strng without any extensions
        String fName = inputFile.getName();
        int pos = fName.lastIndexOf(".");
        if (pos > 0) {
            fName = fName.substring(0, pos);
        }
        System.out.println("inputFile name = " + fName);

		Scanner text = new Scanner(new FileReader(inputFile));
		Scanner key = new Scanner(new FileReader(keyFile));
		// String strText;
		// while (keyFile.hasNext()){
		// 	strText = Integer.valueOf(keyFile.nextLine());
		// }
		// read whole input

		// short[][] stateArray = new short[4][4];
		// // while (text.hasNextShort()){
		// for(int row = 0; row < 4; row++){
		// 	for(int column = 0; column < 4; column++){
		// 		stateArray[row][column] = text.nextShort();
		// 	}
		// }
		
		// array of plaintext
		byte[][] stateArray = {
			{(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00},
			{(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00},
			{(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00},
			{(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00}
		};

		byte[][] invstateArray = {
			{(byte)0x66, (byte)0xef, (byte)0x88, (byte)0xca},
			{(byte)0xe9, (byte)0x8a, (byte)0x4c, (byte)0x34},
			{(byte)0x4b, (byte)0x2c, (byte)0xfa, (byte)0x2b},
			{(byte)0xd4, (byte)0x3b, (byte)0x59, (byte)0x2e}
		};

		// for (short[] arr : stateArray) {
  //           System.out.println(Arrays.toString(arr));
  //       }

		// short[][] keyArray = new short[4][4];
		// while (key.hasNextShort()){
		// 	for(int row = 0; row < 4; row++){
		// 		for(int column = 0; column < 4; column++){
		// 			keyArray[row][column] = key.nextShort();

		// 		}
		// 	}
		// }
		byte[][] keyArray = {
			{(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00},
			{(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00},
			{(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00},
			{(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00}
		};

		byte[][] invkeyArray = {
			{(byte)0xb4, (byte)0x3e, (byte)0x23, (byte)0x6f},
			{(byte)0xef, (byte)0x92, (byte)0xe9, (byte)0x8f},
			{(byte)0x5b, (byte)0xe2, (byte)0x51, (byte)0x18},
			{(byte)0xcb, (byte)0x11, (byte)0xcf, (byte)0x8e}
		};

		//get keysize from terminal input
		keysize = 128;


        //Encryption creates file with extension ".enc"
		if (args[0].equals("e")){
            System.out.println("\nargs[0] = " + args[0] + " = encrypt mode\n");
            File encFile = new File(fName+".enc");
            Encode encode = new Encode(stateArray, keyArray);

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
        //Decryption creates file with extension ".dec"
        else {
        	System.out.println("\nargs[0] = " + args[0] + " = decrypt mode");
            File decFile = new File(fName+".dec");
            Decode decode = new Decode(invstateArray, invkeyArray);

            //number of rounds depends on key size
            if (keysize == 128){
            	//use given cipher key for the initial round
            	decode.invAddRoundKey(10);
            	for(int round=9; round > 0; round--){
            		System.out.println("-------------------round = " + round);
            		//decode.invShiftRows();
		            decode.invSubBytes();
		            decode.invShiftRows();
		            decode.invAddRoundKey(round);
		            decode.invMixColumns();
            	}
            	System.out.println("-------------------round = 0");
            	//decode.invShiftRows();
	            decode.invSubBytes();
	            decode.invShiftRows();
	            decode.invAddRoundKey(0);
            }

        }

        

		System.out.println("\nAES!");
	}

	
}