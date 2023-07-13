package it.saimao.tmk_typing.model;

public class Lesson {
    private int no;
    private String title;


    private String lesson;

    public Lesson(int no, String title, String lesson) {
        this.no = no;
        this.title = title;
        this.lesson = lesson;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLesson() {
        return lesson;
    }

    public void setLesson(String lesson) {
        this.lesson = lesson;
    }

    @Override
    public String toString() {
        return "(" + no + ") - " + title;
    }
}
