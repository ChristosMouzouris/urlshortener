import './App.css';
import { Routes, Route } from 'react-router-dom';
import Home from './pages/Home.tsx';
import Analytics from './pages/Analytics.tsx';
import NavBar from './components/NavBar.tsx';
import NotificationProvider from './components/NotificationContainer.tsx';

function App() {
  return (
    <NotificationProvider>
      <div className={'text-violet-50'}>
        <NavBar />
        <main>
          <Routes>
            <Route path={'/'} element={<Home />} />
            <Route path={'/analytics'} element={<Analytics />} />
          </Routes>
        </main>
      </div>
    </NotificationProvider>
  );
}

export default App;
