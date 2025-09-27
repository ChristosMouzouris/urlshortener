import React from 'react';

interface FrostedCardProps {
  text: string;
}
const FrostedCard: React.FC<FrostedCardProps> = ({ text }) => {
  return (
    <div className="max-w-lg rounded-2xl bg-white/[0.025] backdrop-blur-md border border-white/10 shadow-lg shadow-black/20 p-6 my-2 mx-2">
      <p>{text || ''}</p>
    </div>
  );
};

export default FrostedCard;
