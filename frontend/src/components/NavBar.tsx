import { Link } from 'react-router-dom';
import { HashLink } from 'react-router-hash-link';

const NavBar = () => {
  return (
    <nav className="w-full flex justify-center mt-6">
      <div className="max-w-3xl w-full mx-auto bg-black text-white rounded-full px-6 py-3 flex items-center justify-between shadow-xl shadow-orange-500/40">
        <div className="flex items-center space-x-2">
          <div className="w-12 h-6 bg-white rounded-full flex items-center justify-center text-black font-bold">
            T
          </div>
          <span className="font-semibold text-lg">Tiniest</span>
        </div>
        <div className="hidden md:flex space-x-6">
          <Link to={'/'} className="hover:text-orange-500 transition">
            Create Codes
          </Link>
          <Link to={'/analytics'} className="hover:text-orange-500 transition">
            Analytics
          </Link>
        </div>
        <div className="hidden md:flex">
          <HashLink
            smooth
            to={'#about'}
            className="bg-orange-400 hover:shadow-orange-500 text-white font-semibold px-6 py-2 rounded-full transition shadow-md"
          >
            About
          </HashLink>
        </div>
        <div className="block md:hidden w-12 h-6 bg-white rounded-full items-center justify-center text-black font-bold">
          Menu
        </div>
      </div>
    </nav>
  );
};

export default NavBar;
