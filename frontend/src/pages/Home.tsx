import About from './About.tsx';
import FrostedCard from '../components/FrostedCard.tsx';
import { StatsContainer } from '../components/StatsContainer.tsx';
import { TopUrlsContainer } from '../components/TopUrlsContainer.tsx';
import { UrlShortener } from '../components/UrlShortener.tsx';

function Home() {
  return (
    <div className="mt-10">
      <div className="flex flex-col items-center justify-center text-center space-y-3">
        <p className="font-bold text-5xl text-orange-500">
          Shorten your links! Track your impact!
        </p>
        <p>
          Quick, easy and lightweight service to create short codes for long
          URLs.
        </p>
        <p>Track analytics for each short code.</p>
      </div>
      <div>
        <UrlShortener />
      </div>
      <div className="max-w-6xl mx-auto mt-20 px-4 flex flex-wrap justify-center gap-6">
        <FrostedCard
          heroText="⚡"
          heroTitle="Fast Shortening"
          description="Shorten your URLs in seconds."
        />

        <FrostedCard
          heroText="📈"
          heroTitle="Analytics"
          description="Track clicks for each short code."
        />

        <FrostedCard
          heroText="🤖"
          heroTitle="Filter BOTS"
          description="Shorten your URLs in seconds."
        />

        <FrostedCard
          heroText="⚡"
          heroTitle="Redirect"
          description="Lightining fast redirects."
        />
      </div>
      <div>
        <StatsContainer />
      </div>
      <div>
        <TopUrlsContainer limit={5} />
      </div>
      <div id="about">
        <About />
      </div>
    </div>
  );
}

export default Home;
