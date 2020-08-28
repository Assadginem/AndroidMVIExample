package ginem.com.assadmvi.Model;


// This is to control state
public interface PartialMainState {
    final class Loading implements  PartialMainState{}
    final class GotImageLink implements PartialMainState{
        public String imageLink;

        public GotImageLink(String imageLink){
            this.imageLink = imageLink;
        }
    }

    final class Error implements  PartialMainState{
        public Throwable error;

        public Error(Throwable error){
            this.error = error;
        }
    }
}
