import Accordion from '../components/Accordion.tsx';

function About() {
  return (
    <div className="w-full flex justify-center mt-20">
      <div className="relative w-full max-w-5xl rounded-xl overflow-hidden">
        <div className="absolute inset-0 rounded-xl bg-white/[0.025] backdrop-blur-md border border-white/10 mask-fade-b" />
        <div className="relative z-10">
          <h1 className="text-orange-500 text-center text-3xl font-bold mt-10">
            About
          </h1>
          <div className="flex justify-center items-start my-10 px-4">
            <div className="w-full max-w-4xl rounded-xl bg-white/[0.025] backdrop-blur-md border border-white/10">
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
        </div>
      </div>
    </div>
  );
}

export default About;
