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
			{(byte)0x32, (byte)0x88, (byte)0x31, (byte)0xe0},
			{(byte)0x43, (byte)0x5a, (byte)0x31, (byte)0x37},
			{(byte)0xf6, (byte)0x30, (byte)0x98, (byte)0x07},
			{(byte)0xa8, (byte)0x8d, (byte)0xa2, (byte)0x34}
		};

		byte[][] invstateArray = {
			{(byte)0x39, (byte)0x02, (byte)0xdc, (byte)0x19},
			{(byte)0x25, (byte)0xdc, (byte)0x11, (byte)0x6a},
			{(byte)0x84, (byte)0x09, (byte)0x85, (byte)0x0b},
			{(byte)0x1d, (byte)0xfb, (byte)0x97, (byte)0x32}
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
			{(byte)0x2b, (byte)0x28, (byte)0xab, (byte)0x09},
			{(byte)0x7e, (byte)0xae, (byte)0xf7, (byte)0xcf},
			{(byte)0x15, (byte)0xd2, (byte)0x15, (byte)0x4f},
			{(byte)0x16, (byte)0xa6, (byte)0x88, (byte)0x3c}
		};

		byte[][] invkeyArray = {
			{(byte)0xd0, (byte)0xc9, (byte)0xe1, (byte)0xb6},
			{(byte)0x14, (byte)0xee, (byte)0x3f, (byte)0x63},
			{(byte)0xf9, (byte)0x25, (byte)0x0c, (byte)0x0c},
			{(byte)0xa8, (byte)0x89, (byte)0xc8, (byte)0xa6}
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

            decode.invSubBytes();
            decode.invShiftRows();
            decode.invMixColumns();

        }

        

		System.out.println("\nAES!");
	}

	
}