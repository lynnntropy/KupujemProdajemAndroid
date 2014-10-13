package com.omegavesko.kupujemprodajem;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.omegavesko.kupujemprodajem.R;

import org.jsoup.nodes.Document;

public class MessagesActivity extends Activity  implements NavigationDrawerFragment.NavigationDrawerCallbacks, WebViewFragment.OnFragmentInteractionListener{

    private String email;
    private String password;

    @Override
    public void onNavigationDrawerItemSelected(int position)
    {

    }

    @Override
    public void onFragmentInteraction(Uri uri)
    {

    }

    class PageDownloader extends AsyncTask<Void, Void, Document>
    {

        @Override
        protected Document doInBackground(Void... voids)
        {
            KPLoginHandler.KPCookies userCookies = KPLoginHandler.login(email, password, getApplicationContext());

            Document messagesPage = KPLoginHandler.getPageWithCookies(
                    userCookies,
                    "https://www.kupujemprodajem.com/message.php?action=inbox"
            );

            return messagesPage;
        }

        @Override
        protected void onPostExecute(final Document document)
        {
            final String inboxHtml = document.select("form#inboxForm").first().toString();

            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, WebViewFragment.newInstance(inboxHtml, true))
                            .commit();
                }
            });

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        SharedPreferences prefs = getSharedPreferences("sp", 0);
        this.email = prefs.getString("email", "none");
        this.password = prefs.getString("password", "none");

        if (this.email.equals("none"))
        {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        }
        else
        {
            new PageDownloader().execute();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.messages, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
