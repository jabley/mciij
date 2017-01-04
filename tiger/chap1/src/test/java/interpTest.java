import java.io.StringWriter;
import java.io.PrintWriter;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class interpTest {

    @Test
    public void maxargs() {
      assertThat(prog.prog.maxargs(), is(equalTo(2)));
    }

    @Test
    public void interp() {
      StringWriter buf = new StringWriter();
      interp.interp(prog.prog, new PrintWriter(buf));
      assertThat(buf.toString(), is(equalTo("8\n7\n80\n")));
    }
}
