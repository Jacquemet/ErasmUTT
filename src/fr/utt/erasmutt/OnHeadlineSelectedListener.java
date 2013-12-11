package fr.utt.erasmutt;

//The container Activity must implement this interface so the frag can
//deliver messages
public interface OnHeadlineSelectedListener {
	/** Called by HeadlinesFragment when a list item is selected */
	public void onArticleSelected(int position);
}
