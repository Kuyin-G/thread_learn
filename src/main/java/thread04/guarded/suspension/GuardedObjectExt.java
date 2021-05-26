package thread04.guarded.suspension;

public class GuardedObjectExt extends GuardedObject{
    protected Integer id;
    public GuardedObjectExt(Integer id){
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
