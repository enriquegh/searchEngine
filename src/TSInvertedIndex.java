import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class TSInvertedIndex extends InvertedIndex {

	ReadWriteLock lock;
	public TSInvertedIndex() {
		super();
		lock = new ReadWriteLock();
	}
	
	@Override
	public boolean add(String word, String path, int position) {
		lock.lockWrite();
		boolean add = super.add(word, path, position);
		lock.unlockWrite();
		return add;
	}
	
	@Override
	public boolean addAll(List<String> list, String file, int start) {
		lock.lockWrite();
		boolean add = super.addAll(list, file, start);
		return add;
	}
	
	@Override
	public void print(String output) throws IOException {
		lock.lockRead();
		super.print(output);
		lock.unlockRead();
	}
	
	@Override
	public ArrayList<SearchResult> search(String[] queryList) {
		lock.lockRead();
		ArrayList<SearchResult> search = super.search(queryList);
		lock.unlockRead();
		return search;
	}
}
