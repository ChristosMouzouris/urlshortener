import Accordion from '../components/Accordion.tsx';

function About() {
  return (
    <div className="flex justify-center items-start mt-10 px-4">
      <span className="text-3xl font-bold text-orange-500">About</span>
      <div className="w-full max-w-3xl rounded-xl bg-white/[0.025] backdrop-blur-md border border-white/10">
        <Accordion
          title="This is the first title"
          content="This is the content for the first title"
        />
        <Accordion
          title="This is the second title"
          content="This is the content for the second title"
        />
        <Accordion
          title="This is the third title"
          content="This is the content for the third title"
        />
      </div>
    </div>
  );
}

export default About;
