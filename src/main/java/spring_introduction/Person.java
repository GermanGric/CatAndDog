package spring_introduction;

public class Person {
    private Pet pet;
    private String surename;
    private int age;


    public Person() {
        System.out.println("Person bean is Created");

    }

    public void setPet(Pet pet) {
        System.out.println("Set pet");
        this.pet = pet;
    }

    public void  callYourPet(){
        System.out.println("Hello, my lovely Pet!");
        pet.say();
    }

    public String getSurename() {
        return surename;
    }

    public void setSurename(String surename) {
        System.out.println("setSurname");
        this.surename = surename;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        System.out.println("setAge");
        this.age = age;
    }
}

