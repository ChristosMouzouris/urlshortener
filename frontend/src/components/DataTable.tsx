import React from 'react';

interface TableProps {
  headers: string[];
  rows: (string | bigint)[][];
}
const DataTable: React.FC<TableProps> = ({ headers, rows }) => {
  return (
    <div className="mt-20 flex justify-center">
      <div className="w-full max-w-6xl overflow-x-auto">
        <div>
          <h1 className="text-orange-500 text-center text-3xl font-bold mt-10">
            Top URLs
          </h1>
        </div>
        <table className="w-full mt-5 border border-orange-500 rounded-xl overflow-hidden">
          <thead>
            <tr>
              {headers.map((header) => (
                <th
                  key={header}
                  className="bg-black/[0.2] backdrop-blur-md border border-white/10 px-4 py-2 text-left"
                >
                  {header}
                </th>
              ))}
            </tr>
          </thead>
          <tbody>
            {rows.map((row, i) => (
              <tr key={i} className="hover:bg-black">
                {row.map((cell, j) => (
                  <td
                    key={j}
                    className="bg-white/[0.025] backdrop-blur-md border border-orange-500 px-4 py-2"
                  >
                    {cell}
                  </td>
                ))}
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default DataTable;
