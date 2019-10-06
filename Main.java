/* Building virtual Block Chain system 




*/

public class Main {
	public static void main(String[] args) {
		Node testNode = new Node("testData", "testHash");

		System.out.println(testHashing(testNode));
	}

	public static String testHashing(Node node) {
		String testHashing = Hashing.SHA256(node.pHash + Long.toString(node.getTimeStamp()) + node.getData());
		
		return testHashing;
	}
}
