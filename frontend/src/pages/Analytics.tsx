import { StatsContainer } from '../components/StatsContainer.tsx';
import { TopUrls } from '../components/TopUrls.tsx';
import { BrowserAnalytics } from '../components/BrowserAnalytics.tsx';
import { CountryAnalytics } from '../components/CountryAnalytics.tsx';

function Analytics() {
  return (
    <>
      <div>
        <StatsContainer extended />
      </div>
      <div>
        <TopUrls limit={10} />
      </div>
      <div>
        <BrowserAnalytics />
      </div>
      <div>
        <CountryAnalytics />
      </div>
    </>
  );
}

export default Analytics;
