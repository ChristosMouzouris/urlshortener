export type ApiError =
  | { kind: 'network'; message: string }
  | { kind: 'server'; status: number; message: string }
  | { kind: 'unknown'; message: string };

export async function fetchWrapper<T>(
  input: RequestInfo,
  init?: RequestInit
): Promise<T> {
  let response: Response;

  try {
    response = await fetch(input, init);
  } catch {
    throw {
      kind: 'network',
      message: 'Server unreachable or network error.',
    } as ApiError;
  }

  if (!response.ok) {
    let message = `Request failed with status ${response.status}`;
    try {
      const errorData = await response.json();
      if (errorData?.message) message = errorData.message;
    } catch (_err) {
      void _err;
    }
    throw {
      kind: 'server',
      status: response.status,
      message,
    } as ApiError;
  }

  try {
    return (await response.json()) as T;
  } catch {
    throw {
      kind: 'unknown',
      message: 'Invalid Json in server response.',
    } as ApiError;
  }
}
