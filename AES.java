import java.util.*;
import java.io.*;
import java.lang.Math;


class KeySchedule{
	int[][] keyArray;
	int[][] rCon = {
		{0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 0x36},
		{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		{0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
	};

	KeySchedule(int[][] k){
		keyArray = k;
		System.out.println("Key!");
	}



	public static void RotWord(){

	}
}


class AES{

	//normal execution command: java AES option keyFile inputFile 
	//extra credit execution command: java AES option [length] [mode] keyFile inputFile

	public static File keyFile;
	public static File inputFile;

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
		int[][] stateArray = {
			{0x32, 0x88, 0x31, 0xe0},
			{0x43, 0x5a, 0x31, 0x37},
			{0xf6, 0x30, 0x98, 0x07},
			{0xa8, 0x8d, 0xa2, 0x34}
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
		int[][] keyArray = {
			{0x2b, 0x28, 0xab, 0x09},
			{0x7e, 0xae, 0xf7, 0xcf},
			{0x15, 0xd2, 0x15, 0x4f},
			{0x16, 0xa6, 0x88, 0x3c}
		};

        //If option "e", inputFile is encrypted with the key from keyFile and output an encrypted file w extension ".enc"
		if (args[0].equals("e")){
            System.out.println("\nargs[0] = " + args[0] + " = encrypt mode");
            //encrypt = true;
            File encFile = new File(fName+".enc");
            Encode encode = new Encode(stateArray, keyArray);
            //use given cipher key
            encode.addRoundKey(0);

            //do appropriate number of rounds
            encode.subBytes();
            encode.shiftRows();
            encode.addRoundKey(1);
        }
        //If option "d", inputFile is decrypted with a key from keyFile and output an encrypted file w extension ".dec"
        else {
        	System.out.println("\nargs[0] = " + args[0] + " = decrypt mode");
            //encrypt = false;
            File decFile = new File(fName+".dec");
        }

        

		System.out.println("\nAES!");
	}
}