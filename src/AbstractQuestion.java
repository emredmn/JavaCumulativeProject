
public abstract class AbstractQuestion implements IQuestion {
    protected String text;

    public AbstractQuestion(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }

    // ask() metodu abstract kalacak, çünkü her alt sınıf farklı sorar.
    // Ancak Java'da interface'de tanımlı olduğu için burada tekrar yazmasak da
    // olur.
}