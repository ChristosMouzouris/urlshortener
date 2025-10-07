import { type FC, type FormEvent, useState } from 'react';

interface InputBarProps {
  onInput: (url: string) => Promise<void>;
  placeholder?: string;
}

const InputBar: FC<InputBarProps> = ({ onInput, placeholder }) => {
  const [input, setInput] = useState('');

  const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (input.trim()) {
      onInput(input.trim());
      setInput('');
    }
  };

  return (
    <form onSubmit={handleSubmit} className="w-full max-w-3xl flex">
      <input
        type="text"
        value={input}
        onChange={(e) => setInput(e.target.value)}
        placeholder={placeholder || ''}
        className="flex-grow px-4 rounded-l-full bg-white/[0.025] backdrop-blur-md border border-white/10 focus:outline-none focus:ring-1 focus:ring-orange-500 placeholder-white/50"
      />
      <button
        type="submit"
        className="rounded-r-full bg-orange-400 px-4 py-2 font-bold hover:shadow-orange-500 cursor-pointer focus:ring-2 focus:ring-white"
      >
        Get Code
      </button>
    </form>
  );
};

export default InputBar;
