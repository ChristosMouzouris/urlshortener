import React from 'react';

interface FrostedCardProps {
  heroText: string;
  heroTitle: string;
  description: string;
}
const FrostedCard: React.FC<FrostedCardProps> = ({
  heroText,
  heroTitle,
  description,
}) => {
  return (
    <div className="max-w-[400px] rounded-2xl bg-white/[0.025] backdrop-blur-md border border-white/10 shadow-lg shadow-black/50 text-center p-6">
      <span className="text-4xl ">{heroText || ''}</span>
      <h3 className="font-bold text-2xl text-orange-500 mt-4">
        {heroTitle || ''}
      </h3>
      <p className="mt-2 text-gray-200"> {description || ''}</p>
    </div>
  );
};

export default FrostedCard;
