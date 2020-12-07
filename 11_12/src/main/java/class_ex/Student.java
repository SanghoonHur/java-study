package class_ex;

class Student1{
	String name;
	int eye;
	
	Student1(){}
	Student1(String name, int eye){
		this.name = name;
		this.eye = eye;
	}
	
	void move() {
		System.out.println("�ȴ´�");
	}
}

public class Student {

	public static void main(String[] args) {
		Student1 s1 = new Student1();
		s1.name = "baek";
		s1.eye = 2;
		s1.move();
	}

}
