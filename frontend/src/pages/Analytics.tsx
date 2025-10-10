import { StatsContainer } from '../components/StatsContainer.tsx';
import { TopUrlsContainer } from '../components/TopUrlsContainer.tsx';
import { BrowserAnalyticsContainer } from '../components/BrowserAnalyticsContainer.tsx';
import { CountryAnalyticsContainer } from '../components/CountryAnalyticsContainer.tsx';
import { ClicksTrendContainer } from '../components/ClicksTrendContainer.tsx';

function Analytics() {
  return (
    <>
      <div>
        <StatsContainer extended />
      </div>
      <div>
        <TopUrlsContainer limit={10} />
      </div>
      <div>
        <BrowserAnalyticsContainer />
      </div>
      <div>
        <CountryAnalyticsContainer />
      </div>
      <div>
        <ClicksTrendContainer />
      </div>
    </>
  );
}

export default Analytics;
