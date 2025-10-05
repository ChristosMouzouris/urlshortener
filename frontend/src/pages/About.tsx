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
                title="Overview"
                content="Tiniest is a high performance link management service that transforms long, complex URLs into concise, shareable link.
                It's designed for reliability, speed and scalability. Providing instant redirects and actionable analytics for every shortened URL."
              />
              <Accordion
                title="Performance & Reliability"
                content="Every shortening operation is concurrency-safe, ensuring consistent results even under high request loads.
                The backend is optimised for low-latency and fault tolerance, making it capable of handling high traffic with minimal overhead."
              />
              <Accordion
                title="Analytics & Insights"
                content="The platform tracks detailed click metrics, aggregating data efficiently at the database layer using optimised
                native queries and projections. Users can identify top-performing links, filter bot traffic and gain visibility into
                real engagement patterns."
              />
              <Accordion
                title="Technology Stack"
                content="Built with Java + Spring Boot on the backend and React + TypeScript + Tailwind on the frontend.
                The PostgreSQL database runs in a Docker container for consistent environment setup and easy deployment.
                Persistent storage ensures data integrity and performance."
              />
              <Accordion
                title="Design Philosophy"
                content="The interface emphasises speed, simplicity and responsiveness. All components are mobile first, accessible
                and visually consistent. Designed to demonstrate both frontend craftmanship and backend discipline."
              />
              <Accordion
                title="Future Improvements"
                content="Planned enhancements include user authentication and caching strategies to reduce query overhead
                for frequently accessed URLs."
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default About;
