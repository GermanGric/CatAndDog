package spring_introduction;

public class Dog implements Pet {
    public Dog() {
        System.out.println("Dog is created");
    }

    @Override
    public void say(){
        System.out.println("Bow-Wow-Bow-Wow");
    }
  }
