import java.util.*;
import java.lang.Math;

class Decode{
	
	//holds the current state of the block being encoded
	public static byte[][] stateArray;
	//holds the current version of the key to apply
	public static byte[][] keyArray;
	//keySchedule instance
	public static KeySchedule key;

	public static int[][] invSBox = {
		{0x52, 0x09, 0x6a, 0xd5, 0x30, 0x36, 0xa5, 0x38, 0xbf, 0x40, 0xa3, 0x9e, 0x81, 0xf3, 0xd7, 0xfb},
		{0x7c, 0xe3, 0x39, 0x82, 0x9b, 0x2f, 0xff, 0x87, 0x34, 0x8e, 0x43, 0x44, 0xc4, 0xde, 0xe9, 0xcb},
		{0x54, 0x7b, 0x94, 0x32, 0xa6, 0xc2, 0x23, 0x3d, 0xee, 0x4c, 0x95, 0x0b, 0x42, 0xfa, 0xc3, 0x4e},
		{0x08, 0x2e, 0xa1, 0x66, 0x28, 0xd9, 0x24, 0xb2, 0x76, 0x5b, 0xa2, 0x49, 0x6d, 0x8b, 0xd1, 0x25},
		{0x72, 0xf8, 0xf6, 0x64, 0x86, 0x68, 0x98, 0x16, 0xd4, 0xa4, 0x5c, 0xcc, 0x5d, 0x65, 0xb6, 0x92},
		{0x6c, 0x70, 0x48, 0x50, 0xfd, 0xed, 0xb9, 0xda, 0x5e, 0x15, 0x46, 0x57, 0xa7, 0x8d, 0x9d, 0x84},
		{0x90, 0xd8, 0xab, 0x00, 0x8c, 0xbc, 0xd3, 0x0a, 0xf7, 0xe4, 0x58, 0x05, 0xb8, 0xb3, 0x45, 0x06},
		{0xd0, 0x2c, 0x1e, 0x8f, 0xca, 0x3f, 0x0f, 0x02, 0xc1, 0xaf, 0xbd, 0x03, 0x01, 0x13, 0x8a, 0x6b},
		{0x3a, 0x91, 0x11, 0x41, 0x4f, 0x67, 0xdc, 0xea, 0x97, 0xf2, 0xcf, 0xce, 0xf0, 0xb4, 0xe6, 0x73},
		{0x96, 0xac, 0x74, 0x22, 0xe7, 0xad, 0x35, 0x85, 0xe2, 0xf9, 0x37, 0xe8, 0x1c, 0x75, 0xdf, 0x6e},
		{0x47, 0xf1, 0x1a, 0x71, 0x1d, 0x29, 0xc5, 0x89, 0x6f, 0xb7, 0x62, 0x0e, 0xaa, 0x18, 0xbe, 0x1b},
		{0xfc, 0x56, 0x3e, 0x4b, 0xc6, 0xd2, 0x79, 0x20, 0x9a, 0xdb, 0xc0, 0xfe, 0x78, 0xcd, 0x5a, 0xf4},
		{0x1f, 0xdd, 0xa8, 0x33, 0x88, 0x07, 0xc7, 0x31, 0xb1, 0x12, 0x10, 0x59, 0x27, 0x80, 0xec, 0x5f},
		{0x60, 0x51, 0x7f, 0xa9, 0x19, 0xb5, 0x4a, 0x0d, 0x2d, 0xe5, 0x7a, 0x9f, 0x93, 0xc9, 0x9c, 0xef},
		{0xa0, 0xe0, 0x3b, 0x4d, 0xae, 0x2a, 0xf5, 0xb0, 0xc8, 0xeb, 0xbb, 0x3c, 0x83, 0x53, 0x99, 0x61},
		{0x17, 0x2b, 0x04, 0x7e, 0xba, 0x77, 0xd6, 0x26, 0xe1, 0x69, 0x14, 0x63, 0x55, 0x21, 0x0c, 0x7d},
	};

	//Constructor to decode this block of ciphertext with this given cipherkey
	Decode(byte[][] s, KeySchedule k){
		this.stateArray = s;
		key = k;
		printState();
		System.out.println("stateArray constructed from line of file to decode\n");
	}

	//==================================invSubBytes=====================
	public static void invSubBytes(){
		for(int row = 0; row < 4; row++){
			for(int column = 0; column < 4; column++){
				String hexStr = String.format("%x",stateArray[row][column]).toString();
				//System.out.println("hexstr = " + hexStr);
				char[] charLookup = hexStr.toCharArray();
				//Lookup
				if (charLookup.length == 1){
					int j = Character.getNumericValue(charLookup[0]);
					stateArray[row][column] = (byte)invSBox[0][j];
				}
				else {
					int i = Character.getNumericValue(charLookup[0]);
					int j = Character.getNumericValue(charLookup[1]);
					stateArray[row][column] = (byte)invSBox[i][j];
				}
			}
		}
		printState();
		System.out.println("invSubBytes!\n");
	}

	//==================================invShiftRows=====================
	public static void invShiftRows(){

		int offset = 0;
		byte[] origRow = new byte[4];
		int position;
		for(int row = 0; row < 4; row++){
			//collect original values of this row's columns
			origRow[0] = stateArray[row][0];
			origRow[1] = stateArray[row][1];
			origRow[2] = stateArray[row][2];
			origRow[3] = stateArray[row][3];
			//rotate the row backward
			for(int column = 0; column < 4; column++){
				position = (column+offset)%4;
				stateArray[row][position] = origRow[column];
			}
			//offset is incremented after each row
			offset++;
		}
		printState();
		System.out.println("invShiftRows!\n");
	}

	//==================================invMixColumns=====================
	
	final static int[] LogTable = { 0, 0, 25, 1, 50, 2, 26, 198, 75, 199, 27, 104, 51, 238, 223, 3, 100, 4, 224, 14, 52, 141, 129, 239, 76, 113, 8, 200, 248, 105, 28, 193, 125, 194, 29, 181, 249, 185, 39, 106, 77, 228, 166, 114, 154, 201, 9, 120, 101, 47, 138, 5, 33, 15, 225, 36, 18, 240, 130, 69, 53, 147, 218, 142, 150, 143, 219, 189, 54, 208, 206, 148, 19, 92, 210, 241, 64, 70, 131, 56, 102, 221, 253, 48, 191, 6, 139, 98, 179, 37, 226, 152, 34, 136, 145, 16, 126, 110, 72, 195, 163, 182, 30, 66, 58, 107, 40, 84, 250, 133, 61, 186, 43, 121, 10, 21, 155, 159, 94, 202, 78, 212, 172, 229, 243, 115, 167, 87, 175, 88, 168, 80, 244, 234, 214, 116, 79, 174, 233, 213, 231, 230, 173, 232, 44, 215, 117, 122, 235, 22, 11, 245, 89, 203, 95, 176, 156, 169, 81, 160, 127, 12, 246, 111, 23, 196, 73, 236, 216, 67, 31, 45, 164, 118, 123, 183, 204, 187, 62, 90, 251, 96, 177, 134, 59, 82, 161, 108, 170, 85, 41, 157, 151, 178, 135, 144, 97, 190, 220, 252, 188, 149, 207, 205, 55, 63, 91, 209, 83, 57, 132, 60, 65, 162, 109, 71, 20, 42, 158, 93, 86, 242, 211, 171, 68, 17, 146, 217, 35, 32, 46, 137, 180, 124, 184, 38, 119, 153, 227, 165, 103, 74, 237, 222, 197, 49, 254, 24, 13, 99, 140, 128, 192, 247, 112, 7};
	final static int[] AlogTable = { 1, 3, 5, 15, 17, 51, 85, 255, 26, 46, 114, 150, 161, 248, 19, 53, 95, 225, 56, 72, 216, 115, 149, 164, 247, 2, 6, 10, 30, 34, 102, 170, 229, 52, 92, 228, 55, 89, 235, 38, 106, 190, 217, 112, 144, 171, 230, 49, 83, 245, 4, 12, 20, 60, 68, 204, 79, 209, 104, 184, 211, 110, 178, 205, 76, 212, 103, 169, 224, 59, 77, 215, 98, 166, 241, 8, 24, 40, 120, 136, 131, 158, 185, 208, 107, 189, 220, 127, 129, 152, 179, 206, 73, 219, 118, 154, 181, 196, 87, 249, 16, 48, 80, 240, 11, 29, 39, 105, 187, 214, 97, 163, 254, 25, 43, 125, 135, 146, 173, 236, 47, 113, 147, 174, 233, 32, 96, 160, 251, 22, 58, 78, 210, 109, 183, 194, 93, 231, 50, 86, 250, 21, 63, 65, 195, 94, 226, 61, 71, 201, 64, 192, 91, 237, 44, 116, 156, 191, 218, 117, 159, 186, 213, 100, 172, 239, 42, 126, 130, 157, 188, 223, 122, 142, 137, 128, 155, 182, 193, 88, 232, 35, 101, 175, 234, 37, 111, 177, 200, 67, 197, 84, 252, 31, 33, 99, 165, 244, 7, 9, 27, 45, 119, 153, 176, 203, 70, 202, 69, 207, 74, 222, 121, 139, 134, 145, 168, 227, 62, 66, 198, 81, 243, 14, 18, 54, 90, 238, 41, 123, 141, 140, 143, 138, 133, 148, 167, 242, 13, 23, 57, 75, 221, 124, 132, 151, 162, 253, 28, 36, 108, 180, 199, 82, 246, 1};

	public static void invMixColumns(){
		//******go through columns in this order?
		for(int column = 0; column < 4; column++){
			invMatrixMul(column);
		}
		printState();
		System.out.println("invMixColumns!\n");
	}

	private static byte mul (int a, byte b) { 
		int inda = (a < 0) ? (a + 256) : a; 
		int indb = (b < 0) ? (b + 256) : b;

		if ( (a != 0) && (b != 0) ) { 
			int index = (LogTable[inda] + LogTable[indb]); 
			byte val = (byte)(AlogTable[ index % 255 ] ); 
			return val; 
		} 
		else
			return 0;
	} // function provided by Dr. Young

	public static void invMatrixMul(int c) { 
		byte a[] = new byte[4];

		// note that a is justateArray a copy of stateArray[.][c] 
		for (int i = 0; i < 4; i++) 
			a[i] = stateArray[i][c];

		stateArray[0][c] = (byte)(mul(0xE,a[0]) ^ mul(0xB,a[1]) ^ mul(0xD, a[2]) ^ mul(0x9,a[3])); 
		stateArray[1][c] = (byte)(mul(0xE,a[1]) ^ mul(0xB,a[2]) ^ mul(0xD, a[3]) ^ mul(0x9,a[0])); 
		stateArray[2][c] = (byte)(mul(0xE,a[2]) ^ mul(0xB,a[3]) ^ mul(0xD, a[0]) ^ mul(0x9,a[1])); 
		stateArray[3][c] = (byte)(mul(0xE,a[3]) ^ mul(0xB,a[0]) ^ mul(0xD, a[1]) ^ mul(0x9,a[2])); 
	} // function provided by Dr. Young

	//==================================invAddRoundKey=====================
	public static void invAddRoundKey(int round){
		
		//retrieves the next 4x4 keyArray by grabbing the appropriate columns from the expandedKeyArry
		keyArray = key.decodeNextKey(round);

		//XOR columns of stateArray and key
		for(int column = 0; column < 4; column++){
			//Get column of interest from both key and state
			for(int row = 0; row < 4; row++){
				//XOR the current columns
				stateArray[row][column] = (byte)(stateArray[row][column] ^ keyArray[row][column]);
				//System.out.println("result["+row+"]["+column+"] = " + String.format("%x",stateArray[row][column]).toString());
			}
		}
		printKey();
		System.out.println("key applied\n");
		printState();
		System.out.println("invAddRoundKey!\n");
	}





	public static void printState(){
		for(int row = 0; row < 4; row++){
			for(int column = 0; column < 4; column++){
				String hexStr = String.format("%x",stateArray[row][column]).toString();
				System.out.print(hexStr + " ");
			}
			System.out.println("");
		}
	}
	public static void printKey(){
		for(int row = 0; row < 4; row++){
			for(int column = 0; column < 4; column++){
				String hexStr = String.format("%x",keyArray[row][column]).toString();
				System.out.print(hexStr + " ");
			}
			System.out.println("");
		}
	}
	public static void printIntArr(byte[] ia){
		for (int i = 0; i < ia.length; i++){
			String hexStr = String.format("%x",ia[i]).toString();
			System.out.print(hexStr + " ");
		}
		System.out.println("");
	}
}