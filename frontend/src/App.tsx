import './App.css';
import { Routes, Route } from 'react-router-dom';
import Home from './pages/Home.tsx';
import Analytics from './pages/Analytics.tsx';
import About from './pages/About.tsx';
import NavBar from './components/NavBar.tsx';

function App() {
  return (
    <div className={'text-violet-50'}>
      <NavBar />
      <main>
        <Routes>
          <Route path={'/'} element={<Home />} />
          <Route path={'/analytics'} element={<Analytics />} />
          <Route path={'/about'} element={<About />} />
        </Routes>
      </main>
    </div>
  );
}

export default App;
