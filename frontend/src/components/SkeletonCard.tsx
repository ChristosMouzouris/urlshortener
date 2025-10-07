import React from 'react';

interface SkeletonCardProps {
  lines?: number;
  height?: string;
  className?: string;
}

export const SkeletonCard: React.FC<SkeletonCardProps> = ({
  lines = 3,
  height = 'h-32',
  className = '',
}) => {
  return (
    <div
      className={`max-w-6xl mx-4 md:mx-auto rounded-2xl bg-neutral-800 p-4 animate-pulse shadow-sm ${height} ${className} mt-20`}
    >
      <div className="space-y-3">
        {[...Array(lines)].map((_, i) => (
          <div
            key={i}
            className="h-4 bg-neutral-700 rounded"
            style={{ width: `${80 - i * 10}%` }}
          />
        ))}
      </div>
    </div>
  );
};
