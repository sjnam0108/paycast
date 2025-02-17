package kr.co.paycast.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

public class UnicodeBOMInputStream extends InputStream {
	public static final class BOM {
	    /**
	     * NONE.
	     */
		public static final BOM NONE = new BOM(new byte[]{},"NONE");

	    /**
	     * UTF-8 BOM (EF BB BF).
	     */
	    public static final BOM UTF_8 = new BOM(new byte[]{(byte)0xEF, (byte)0xBB, (byte)0xBF}, "UTF-8");

	    /**
	     * UTF-16, little-endian (FF FE).
	     */
	    public static final BOM UTF_16_LE = new BOM(new byte[]{ (byte)0xFF, (byte)0xFE}, "UTF-16 little-endian");

	    /**
	     * UTF-16, big-endian (FE FF).
	     */
	    public static final BOM UTF_16_BE = new BOM(new byte[]{ (byte)0xFE, (byte)0xFF}, "UTF-16 big-endian");

	    /**
	     * UTF-32, little-endian (FF FE 00 00).
	     */
	    public static final BOM UTF_32_LE = new BOM(new byte[]{ (byte)0xFF, (byte)0xFE, (byte)0x00, (byte)0x00},
	                                                "UTF-32 little-endian");

	    /**
	     * UTF-32, big-endian (00 00 FE FF).
	     */
	    public static final BOM UTF_32_BE = new BOM(new byte[]{ (byte)0x00, (byte)0x00, (byte)0xFE, (byte)0xFF},
	                                                "UTF-32 big-endian");

	    public final String toString() {
	    	return description;
	    }

	    public final byte[] getBytes() {
	    	final int     length = bytes.length;
	    	final byte[]  result = new byte[length];

	    	System.arraycopy(bytes,0,result,0,length);

	    	return result;
	    }

	    private BOM(final byte bom[], final String description) {
	    	assert(bom != null)               : "invalid BOM: null is not allowed";
	    	assert(description != null)       : "invalid description: null is not allowed";
	    	assert(description.length() != 0) : "invalid description: empty string is not allowed";

	    	this.bytes          = bom;
	    	this.description  = description;
	    }

	    final byte    bytes[];
	    private final String  description;
	} // BOM

	public UnicodeBOMInputStream(final InputStream inputStream) throws NullPointerException, IOException {
		if (inputStream == null) {
			throw new NullPointerException("invalid input stream: null is not allowed");
		}
		
		in = new PushbackInputStream(inputStream,4);

		final byte  bom[] = new byte[4];
		final int   read  = in.read(bom);
		
	    switch(read) {
	    case 4:
	    	if (bom[0] == (byte)0xFF && bom[1] == (byte)0xFE && bom[2] == (byte)0x00 && bom[3] == (byte)0x00) {
	    		this.bom = BOM.UTF_32_LE;
	    		break;
	    	} else if (bom[0] == (byte)0x00 && bom[1] == (byte)0x00 && bom[2] == (byte)0xFE && bom[3] == (byte)0xFF) {
	    		this.bom = BOM.UTF_32_BE;
	    		break;
	    	}
	    case 3:
	    	if (bom[0] == (byte)0xEF && bom[1] == (byte)0xBB && bom[2] == (byte)0xBF) {
	    		this.bom = BOM.UTF_8;
	    		break;
	    	}
	    case 2:
	    	if (bom[0] == (byte)0xFF && bom[1] == (byte)0xFE) {
	    		this.bom = BOM.UTF_16_LE;
	    		break;
	    	} else if (bom[0] == (byte)0xFE && bom[1] == (byte)0xFF) {
	    		this.bom = BOM.UTF_16_BE;
	    		break;
	        }
	    default:
	    	this.bom = BOM.NONE;
	    	break;
	    }

	    if (read > 0) {
	    	in.unread(bom,0,read);
	    }
	}

	public final BOM getBOM() {
		// BOM type is immutable.
		return bom;
	}

	public final synchronized UnicodeBOMInputStream skipBOM() throws IOException {
		if (!skipped) {
			in.skip(bom.bytes.length);
			skipped = true;
		}

		return this;
	}

	public int read() throws IOException {
		return in.read();
	}

	public int read(final byte b[]) throws IOException, NullPointerException {
		return in.read(b,0,b.length);
	}

	public int read(final byte b[], final int off, final int len) throws IOException, NullPointerException {
	    return in.read(b,off,len);
	}

	public long skip(final long n) throws IOException {
	    return in.skip(n);
	}

	public int available() throws IOException {
		return in.available();
	}

	public void close() throws IOException {
	    in.close();
	}

	public synchronized void mark(final int readlimit) {
	    in.mark(readlimit);
	}

	public synchronized void reset() throws IOException {
	    in.reset();
	}

	public boolean markSupported() {
	    return in.markSupported();
	}

	private final PushbackInputStream in;
	private final BOM bom;
	private boolean skipped = false;
}
