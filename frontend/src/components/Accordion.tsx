import React, { useState } from 'react';

interface AccordionProps {
  title: string;
  content: string;
}
const Accordion: React.FC<AccordionProps> = ({ title, content }) => {
  const [accordionOpen, setAccordionOpen] = useState(false);
  return (
    <div className="w-full shadow-lg shadow-black/20 p-6">
      <button
        className="flex justify-between w-full"
        onClick={() => setAccordionOpen(!accordionOpen)}
      >
        <span className="text-lg font-bold">{title}</span>
        {accordionOpen ? <span className="rotate-45">+</span> : <span>+</span>}
      </button>
      <div
        className={`grid overflow-hidden transition-all duration-300 ease-in-out text-sm ${
          accordionOpen
            ? `grid-rows-[1fr] opacity-100`
            : `grid-rows-[0fr] opacity-0`
        }`}
      >
        <div className="overflow-hidden">{content}</div>
      </div>
    </div>
  );
};

export default Accordion;
