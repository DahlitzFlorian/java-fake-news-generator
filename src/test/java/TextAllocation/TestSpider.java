package TextAllocation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestSpider {

    /**
     * This is our test. It creates a spider (which creates spider legs) and crawls the web.
     */
    @Test
    public void testSpider() {
        Spider spider = new Spider();
        spider.search("http://www.spiegel.de/", "politik");

        assertEquals(7, 7);
    }
}